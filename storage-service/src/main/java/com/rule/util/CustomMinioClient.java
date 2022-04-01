package com.rule.util;

import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.rule.common.ResultMessage;
import com.rule.pojo.MinioFile;
import io.minio.BucketExistsArgs;
import io.minio.CreateMultipartUploadResponse;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListPartsResponse;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.ObjectWriteResponse;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.ServerException;
import io.minio.errors.XmlParserException;
import io.minio.http.Method;
import io.minio.messages.Part;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 自定义minio
 *
 * @author tuine
 * @date 2021-03-23
 */
@Slf4j
public class CustomMinioClient extends MinioClient {

    public CustomMinioClient(MinioClient client) {
        super(client);
    }

    public String initMultiPartUpload(String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException, ErrorResponseException {
        CreateMultipartUploadResponse response = this.createMultipartUpload(bucket, region, object, headers, extraQueryParams);
        return response.result().uploadId();
    }

    public CreateMultipartUploadResponse createMultipartUpload(String bucket, String region, String object, Multimap<String, String> headers, Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException, ErrorResponseException {
        return this.createMultipartUpload(bucket, region, object, headers, extraQueryParams);
    }

    public ObjectWriteResponse mergeMultipartUpload(String bucketName, String region, String objectName, String uploadId, Part[] parts, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException, ErrorResponseException {

        return this.completeMultipartUpload(bucketName, region, objectName, uploadId, parts, extraHeaders, extraQueryParams);
    }

    public ListPartsResponse listMultipart(String bucketName, String region, String objectName, Integer maxParts, Integer partNumberMarker, String uploadId, Multimap<String, String> extraHeaders, Multimap<String, String> extraQueryParams) throws NoSuchAlgorithmException, InsufficientDataException, IOException, InvalidKeyException, ServerException, XmlParserException, ErrorResponseException, InternalException, InvalidResponseException {

        return this.listParts(bucketName, region, objectName, maxParts, partNumberMarker, uploadId, extraHeaders, extraQueryParams);
    }

    public CreateMultipartUploadResponse uploadMultipart(String bucketName,
                                                         String region,
                                                         String objectName,
                                                         Multimap<String, String> headers,
                                                         Multimap<String, String> extraQueryParams) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InsufficientDataException, ServerException, InternalException, XmlParserException, InvalidResponseException, ErrorResponseException {
        return this.createMultipartUpload(bucketName, region, objectName, headers, extraQueryParams);
    }

    public String getUploadId(String bucketName, String region, String objectName,
                              Multimap<String, String> headers, Multimap<String, String> extraQueryParams)
            throws ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, IOException, InvalidKeyException, XmlParserException, InvalidResponseException, InternalException {
        CreateMultipartUploadResponse response = this.createMultipartUpload(bucketName, region, objectName, headers, extraQueryParams);

        return response.result().uploadId();
    }

    /**
     * 初始化获取 uploadId
     *
     * @param objectName  文件名
     * @param partCount   分片总数
     * @param contentType contentType
     * @return
     */
    public MinioFile createMinioFile(String bucket, String objectName, int partCount, String contentType) {

        this.checkBucket(bucket);

        HashMultimap<String, String> headers = HashMultimap.create();
        if (StrUtil.isBlank(contentType)) {
            contentType = "application/octet-stream";
        }
        headers.put("Content-Type", contentType);
        int expireDuration = 60 * 60 * 24;
        String uploadId;
        List<String> partUrlList = new ArrayList<>();

        try {
            // 获取 uploadId
            uploadId = this.getUploadId(bucket,
                    null,
                    objectName,
                    headers,
                    null);
            Map<String, String> paramsMap = new HashMap<>(2);
            paramsMap.put("uploadId", uploadId);
            for (int i = 1; i <= partCount; i++) {
                paramsMap.put("partNumber", String.valueOf(i));
                // 获取上传 url
                String uploadUrl = this.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                        // 注意此处指定请求方法为 PUT，前端需对应，否则会报 `SignatureDoesNotMatch` 错误
                        .method(Method.PUT)
                        .bucket(bucket)
                        .object(objectName)
                        // 指定上传连接有效期, 默认为1天
                        .expiry(expireDuration, TimeUnit.SECONDS)
                        .extraQueryParams(paramsMap).build());
                partUrlList.add(uploadUrl);
            }
        } catch (Exception e) {
            log.error("initMultiPartUpload Error:" + e);
            return null;
        }
        // 过期时间
        LocalDateTime expireTime = LocalDateTimeUtil.offset(LocalDateTime.now(), expireDuration, ChronoUnit.SECONDS);
        MinioFile result = new MinioFile();
        result.setUploadId(uploadId);
        result.setExpireDatetime(expireTime);
        result.setPartUploadUrls(partUrlList);
        return result;
    }

    public boolean checkBucket(String bucket) {
        if (!StringUtils.hasText(bucket)) {
            throw new RuntimeException(" bucket is not empty!! ");
        }
        boolean exist = false;
        try {
            exist = bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!exist) {
                this.makeBucket(
                        MakeBucketArgs.builder()
                                .bucket(bucket)
                                .build());
            }
            return ResultMessage.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exist;
    }


    /*public Map<String, Object> initMultiPartUpload(String objectName, int partCount, String contentType) {
        Map<String, Object> result = new HashMap<>();
        try {
            if (StrUtil.isBlank(contentType)) {
                contentType = "application/octet-stream";
            }
            HashMultimap<String, String> headers = HashMultimap.create();
            headers.put("Content-Type", contentType);
            String uploadId = customMinioClient.initMultiPartUpload(minioProperties.getBucket(), null, objectName, headers, null);

            result.put("uploadId", uploadId);
            List<String> partList = new ArrayList<>();

            Map<String, String> reqParams = new HashMap<>();
            //reqParams.put("response-content-type", "application/json");
            reqParams.put("uploadId", uploadId);
            for (int i = 1; i <= partCount; i++) {
                reqParams.put("partNumber", String.valueOf(i));
                String uploadUrl = customMinioClient.getPresignedObjectUrl(
                        GetPresignedObjectUrlArgs.builder()
                                .method(Method.PUT)
                                .bucket(minioProperties.getBucket())
                                .object(objectName)
                                .expiry(1, TimeUnit.DAYS)
                                .extraQueryParams(reqParams)
                                .build());
                partList.add(uploadUrl);
            }
            result.put("uploadUrls", partList);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return result;
    }*/
}
