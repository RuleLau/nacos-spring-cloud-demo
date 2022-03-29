package com.rule.api;

import com.alibaba.fastjson.JSON;
import com.rule.pojo.RspResult;
import com.rule.service.MinioService;
import com.rule.util.CustomMinioClient;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.UploadObjectArgs;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

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

    public void get(List<String> md5List,  String bucketName, String fileName, Integer partSize, Long fileSize, String mimetype, String fileExt) throws IOException, NoSuchAlgorithmException, InvalidResponseException, InvalidKeyException, ServerException, ErrorResponseException, XmlParserException, InsufficientDataException, InternalException {
        // 前端进行分片，根据 size 分片，然后将
        customMinioClient.uploadMultipart(bucketName, null, fileName, null, null);
    }

    public static void main(String[] args) throws Exception {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://192.168.25.23:9000")
                .credentials("admin", "12345678")
                .build();
        CustomMinioClient customMinioClient = new CustomMinioClient(minioClient);
        UploadObjectArgs objectArgs = UploadObjectArgs.builder()
                .filename("C:\\Users\\rulelau\\Desktop\\a.txt", 5 * 1024 * 1024)
                .bucket("test")
                .object("12222")
                .build();
        ObjectWriteResponse objectWriteResponse = customMinioClient.uploadObject(objectArgs);
        System.out.println(JSON.toJSONString(objectWriteResponse));
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
