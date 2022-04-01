package com.rule.dao;

import com.rule.pojo.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadDao extends JpaRepository<FileUpload, Integer> {

    FileUpload findByUploadId(String uploadId);

    FileUpload findByMd5(String md5);
}
