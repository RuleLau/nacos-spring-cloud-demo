package com.rule.dao;

import com.rule.pojo.FileChunk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileChunkDao extends JpaRepository<FileChunk, Integer> {

    List<FileChunk> findAllByUploadId(String uploadId);
}
