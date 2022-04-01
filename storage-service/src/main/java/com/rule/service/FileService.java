package com.rule.service;

import com.rule.pojo.FileResult;
import com.rule.pojo.FileUpload;
import com.rule.pojo.RspResult;

public interface FileService {

    public FileResult uploadFile(String fileMd5, String filename, String bucket, Double fileSize, String fileType);

    public FileUpload checkFileMd5(String fileMd5);

    public int chunkFile(long fileSize, String filename, String bucket);

    public RspResult mergeFile(String uploadId);
}
