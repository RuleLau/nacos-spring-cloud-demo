package com.rule.pojo;

import java.time.ZonedDateTime;

public class FileChunk {

    private Integer oid;

    private String uploadId;

    private String chunkUploadUrl;

    private Integer status;

    private ZonedDateTime entryDatetime;

    private ZonedDateTime updateDatetime;


    public Integer getOid() {
        return oid;
    }

    public void setOid(Integer oid) {
        this.oid = oid;
    }

    public String getUploadId() {
        return uploadId;
    }

    public void setUploadId(String uploadId) {
        this.uploadId = uploadId;
    }

    public String getChunkUploadUrl() {
        return chunkUploadUrl;
    }

    public void setChunkUploadUrl(String chunkUploadUrl) {
        this.chunkUploadUrl = chunkUploadUrl;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public ZonedDateTime getEntryDatetime() {
        return entryDatetime;
    }

    public void setEntryDatetime(ZonedDateTime entryDatetime) {
        this.entryDatetime = entryDatetime;
    }

    public ZonedDateTime getUpdateDatetime() {
        return updateDatetime;
    }

    public void setUpdateDatetime(ZonedDateTime updateDatetime) {
        this.updateDatetime = updateDatetime;
    }
}
