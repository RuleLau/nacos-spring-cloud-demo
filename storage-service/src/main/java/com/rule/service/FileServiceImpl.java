package com.rule.service;

import com.alibaba.fastjson.JSON;
import com.rule.dao.FileChunkDao;
import com.rule.dao.FileUploadDao;
import com.rule.pojo.FileChunk;
import com.rule.pojo.FileResult;
import com.rule.pojo.FileUpload;
import com.rule.pojo.MinioFile;
import com.rule.pojo.RspResult;
import com.rule.util.CustomMinioClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.rule.common.ResultMessage.FILE_CHUNK_SIZE;
import static com.rule.common.ResultMessage.UPLOAD_FAILED;
import static com.rule.common.ResultMessage.UPLOAD_PROCESS;
import static com.rule.common.ResultMessage.UPLOAD_SUCCESS;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private CustomMinioClient customMinioClient;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private FileUploadDao fileUploadDao;

    @Autowired
    private FileChunkDao fileChunkDao;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public FileResult uploadFile(String fileMd5, String filename, String bucket, Double fileSize, String fileType) {
        FileResult fileResult = new FileResult();
        // 1. 检查文件是否存在
        FileUpload fileUpload = checkFileMd5(fileMd5);
        if (fileUpload == null || fileUpload.getStatus() == UPLOAD_FAILED) {
            // 重新上传
            if (fileUpload == null) {
                double partCount = Math.ceil(fileSize / FILE_CHUNK_SIZE);
                int intPart = (int) partCount;
                log.info(" 文件分片数量为：" + partCount);
                MinioFile minioFile = customMinioClient.createMinioFile(bucket, filename, intPart, fileType);
                // 保存到 mysql
                fileUpload = new FileUpload();
                fileUploadDao.save(fileUpload);

                // 返回结果
                fileResult.setUploadId(minioFile.getUploadId());
                fileResult.setStatus(UPLOAD_PROCESS);
                fileResult.setChunkUploadUrls(minioFile.getPartUploadUrls());
                fileResult.setChunks(intPart);
            }
            return fileResult;
        }

        String uploadId = fileUpload.getUploadId();
        fileResult.setUploadId(uploadId);
        // 已经上传过的文件直接返回
        if (fileUpload.getStatus() == UPLOAD_SUCCESS) {
            fileResult.setStatus(UPLOAD_SUCCESS);
            return fileResult;
        }

        // 上传了一半，需要重新上传未上传的文件
        List<FileChunk> fileChunkList = fileChunkDao.findAllByUploadId(uploadId);
        List<String> failedFileChunks = fileChunkList.stream()
                .filter(item -> item.getStatus() != UPLOAD_SUCCESS)
                .map(FileChunk::getChunkUploadUrl)
                .collect(Collectors.toList());
        fileResult.setChunks(fileChunkList.size());
        fileResult.setChunkUploadUrls(failedFileChunks);
        fileResult.setStatus(UPLOAD_PROCESS);
        return fileResult;
    }

    @Override
    public FileUpload checkFileMd5(String fileMd5) {
        // 从 redis 中查询
        String uploadObj = redisTemplate.opsForValue().get(fileMd5);
        FileUpload fileUpload;
        if (uploadObj == null) {
            // 从 mysql 中查询
            fileUpload = fileUploadDao.findByMd5(fileMd5);
        } else {
            fileUpload = JSON.parseObject(uploadObj, FileUpload.class);
        }
        // 上传不成功，重新上传
        return fileUpload;
    }

    @Override
    public int chunkFile(long fileSize, String filename, String bucket) {
        return 0;
    }

    @Override
    public RspResult mergeFile(String uploadId) {
        return null;
    }

    public static void main(String[] args) {
        double s = 5d;
        System.out.println(Math.ceil(s/2));
    }
}
