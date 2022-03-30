package com.rule.api;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Multimap;
import com.rule.pojo.RspResult;
import com.rule.service.MinioService;
import com.rule.util.CustomMinioClient;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.PostPolicy;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class StorageApi {

/*
    @RequestMapping("getUploadPath")
    public List<String> getUploadPath(@RequestBody UploadDto uploadDto) {
        List<String> urlList = new ArrayList<>();
        for (int i = 1; i <= uploadDto.getChunkCount(); i++) {
            String url = minIoUtil.createUploadUrl(uploadDto.getBucketName(), uploadDto.getFileName(), i, uploadDto.getUploadId());
            urlList.add(url);
        }
        log.info("urlList:{}", urlList);
        return urlList;
    }

    @SneakyThrows
    public String createUploadUrl(String bucketName, String objectName, Integer partNumber, String uploadId) {
        return myMinioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.PUT)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(604800)
                        .extraQueryParams(
                                newMultimap("partNumber", Integer.toString(partNumber), "uploadId", uploadId)
                        )
                        .build()
        );
    }

    @RequestMapping("completeFile")
    public void completeMultipartUpload(@RequestBody UploadDto uploadDto) {
        ListPartsResponse listPartsResponse = minIoUtil.listParts(uploadDto.getBucketName(),
                uploadDto.getFileName(),
                uploadDto.getUploadId());
        List<Part> parts = listPartsResponse.result().partList();
        minIoUtil.completeMultipartUpload(uploadDto.getBucketName(),
                uploadDto.getFileName(),
                uploadDto.getUploadId(),
                parts.toArray(new Part[]{}));
    }
*/

    @Autowired
    private MinioService minioService;

    @Autowired
    private CustomMinioClient customMinioClient;

    public void get(String bucketName, String fileName, Integer partCount) throws Exception {
        // 前端进行分片，根据 size 分片，然后将
        customMinioClient.uploadMultipart(bucketName, null, fileName, null, null);
    }

    public static void main(String[] args) throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://127.0.0.1:9000")
                .credentials("minioadmin", "minioadmin")
                .build();
        CustomMinioClient customMinioClient = new CustomMinioClient(minioClient);
        //String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams
        String uploadId = customMinioClient.initMultiPartUpload("test", null, "b.txt", null, null);
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        List<String> partList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();
        reqParams.put("response-content-type", "application/json");

        // 为存储桶创建一个上传策略，过期时间为7天
        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusDays(1));
        // 设置一个参数key，值为上传对象的名称
        policy.addEqualsCondition("key", fileName);
        // 添加Content-Type，例如以"image/"开头，表示只能上传照片，这里吃吃所有
        policy.addStartsWithCondition("Content-Type", MediaType.ALL_VALUE);
        // 设置上传文件的大小 64kiB to 10MiB.
        //policy.addContentLengthRangeCondition(64 * 1024, 10 * 1024 * 1024);


        for (int i = 1; i <= 1; i++) {
            reqParams.put("partNumber", String.valueOf(i));
            String uploadUrl = customMinioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket("test")
                            .object("b.txt")
                            .expiry(60 * 60 * 24)
                            .extraQueryParams(reqParams)
                            .build());
            partList.add(uploadUrl);
        }
        result.put("uploadUrls", partList);
        System.out.println(JSON.toJSONString(result));
    }

    public Map<String, String> getPresignedPostFormData(String bucketName, String fileName, CustomMinioClient customMinioClient) throws Exception {
        // 为存储桶创建一个上传策略，过期时间为7天
        PostPolicy policy = new PostPolicy(bucketName, ZonedDateTime.now().plusDays(1));
        // 设置一个参数key，值为上传对象的名称
        policy.addEqualsCondition("key", fileName);
        // 添加Content-Type，例如以"image/"开头，表示只能上传照片，这里吃吃所有
        policy.addStartsWithCondition("Content-Type", MediaType.ALL_VALUE);
        // 设置上传文件的大小 64kiB to 10MiB.
        //policy.addContentLengthRangeCondition(64 * 1024, 10 * 1024 * 1024);
        return customMinioClient.getPresignedPostFormData(policy);
    }

    public String generateOssUuidFileName(String originalFilename) {
        return "files" + StrUtil.SLASH + DateUtil.format(new Date(), "yyyy-MM-dd") + StrUtil.SLASH + UUID.randomUUID() + StrUtil.UNDERLINE + originalFilename;
    }

    /**
     * 上传分片上传请求，返回uploadId
     */
    public CreateMultipartUploadResponse uploadId(String bucketName, String region, String objectName,
                                                  Multimap<String, String> headers, Multimap<String, String> extraQueryParams, CustomMinioClient customMinioClient) throws Exception {
        return customMinioClient.createMultipartUpload(bucketName, region, objectName, headers, extraQueryParams);
    }

    /**
     * 返回分片上传需要的签名数据URL及 uploadId
     *
     * @param bucketName
     * @param fileName
     * @return
     */
    @GetMapping("/createMultipartUpload")
    @ResponseBody
    public Map<String, Object> createMultipartUpload(String bucketName, String fileName, Integer chunkSize, CustomMinioClient customMinioClient) {
        // 1. 根据文件名创建签名
        Map<String, Object> result = new HashMap<>();
        // 2. 获取uploadId
        CreateMultipartUploadResponse response = uploadId(bucketName, null, fileName, null, null, customMinioClient);
        String uploadId = response.result().uploadId();
        result.put("uploadId", uploadId);
        // 3. 请求Minio 服务，获取每个分块带签名的上传URL
        Map<String, String> reqParams = new HashMap<>();
        reqParams.put("uploadId", uploadId);
        List<String> partList = new ArrayList<>();
        // 4. 循环分块数 从1开始
        for (int i = 1; i <= chunkSize; i++) {
            reqParams.put("partNumber", String.valueOf(i));
            String uploadUrl = customMinioClient.getPresignedObjectUrl(bucketName, fileName, reqParams);// 获取URL
            result.put("chunk_" + (i - 1), uploadUrl); // 添加到集合
        }
        return result;
    }

    /**
     * 文件上传前注册  校验  mimetype文件类型  fileExt 文件扩展名
     */
    @RequestMapping("/register")
    public RspResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return minioService.register(fileMd5, fileName, fileSize, mimetype, fileExt);
    }

    //校验分块文件是否存在 chunk下标
    @RequestMapping("/checkchunk")
    public RspResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        return minioService.checkChunk(fileMd5, chunk, chunkSize);
    }

    //上传分块
    @RequestMapping("/uploadchunk")
    public RspResult uploadChunk(MultipartFile file, String fileMd5, Integer chunk) {
        return minioService.uploadChunk(file, fileMd5, chunk);
    }

    //合并分块
    @RequestMapping("/mergechunks")
    public RspResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        return minioService.mergeChunks(fileMd5, fileName, fileSize, mimetype, fileExt);
    }

    //文件下载
    @RequestMapping("/download")
    public void download(HttpServletResponse response, String bucketName, String filename) {
        minioService.download(response, bucketName, filename);
    }

    //文件删除
    @RequestMapping("/delete")
    public RspResult delete(String bucketName, String filename) {
        return minioService.delete(bucketName, filename);
    }

    //文件预览
    @RequestMapping("/preview")
    public void preview(HttpServletResponse response, String bucketName, String filename) throws Exception {

        minioService.preview(response, bucketName, filename);
    }


}
