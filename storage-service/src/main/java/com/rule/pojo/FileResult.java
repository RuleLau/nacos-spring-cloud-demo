package com.rule.pojo;

import java.util.List;

public class FileResult {

    private String uploadId;

    private Integer chunks;

    private Integer status;

    private List<String> chunkUploadUrls;

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<String> getChunkUploadUrls() {
        return chunkUploadUrls;
    }

    public void setChunkUploadUrls(List<String> chunkUploadUrls) {
        this.chunkUploadUrls = chunkUploadUrls;
    }
}
