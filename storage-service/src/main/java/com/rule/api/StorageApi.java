package com.rule.api;

import com.rule.pojo.RspResult;
import com.rule.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

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
