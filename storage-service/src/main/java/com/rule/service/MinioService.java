package com.rule.service;

import com.rule.common.ResultMessage;
import com.rule.pojo.RspResult;
import com.rule.util.MinioUtil;
import io.minio.StatObjectResponse;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MinioService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private MinioUtil minioUtil;

    @Value("${minio.bucketName}")
    private String bucketName;


    //文件校验
    public RspResult register(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        RspResult result = new RspResult();
        //1.校验文件md5是否存在
        String s = (String) redisTemplate.opsForValue().get(fileMd5);
        if (!StringUtils.isEmpty(s)) {
            result.setFlag(ResultMessage.TRUE);
            result.setMessage(ResultMessage.FILE_EXIST);
            return result;
        }
        //2.创建临时桶
        boolean b = minioUtil.bucketExists(fileMd5);
        if (b) {
            //存在先删除在创建
            minioUtil.removeBucket(fileMd5);
        }
        minioUtil.createBucket(fileMd5);
        result.setFlag(ResultMessage.FALSE);
        result.setMessage(ResultMessage.FILE_NOT_EXIST);
        return result;
    }

    //校验分块是否存在
    public RspResult checkChunk(String fileMd5, Integer chunk, Integer chunkSize) {
        RspResult result = new RspResult();
        try {
            List<Object> folderList = minioUtil.getFolderList(fileMd5);
            List<String> fileNames = this.getFileNames(folderList);
            if (fileNames != null && !fileNames.isEmpty()) {
                for (String fileName : fileNames) {
                    if (fileName.equals(chunk + "")) {
                        result.setFlag(ResultMessage.TRUE);
                        result.setMessage(ResultMessage.CHUNK_EXIST);
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setFlag(ResultMessage.FALSE);
        result.setMessage(ResultMessage.CHUNK_NOT_EXIST);
        return result;
    }

    //上传分块
    public RspResult uploadChunk(MultipartFile file, String fileMd5, Integer chunk) {
        RspResult result = new RspResult();
        minioUtil.upload(fileMd5, file, chunk + "");
        result.setFlag(ResultMessage.TRUE);
        result.setMessage(ResultMessage.CHUNK_UPLOAD_SUCCESS);
        return result;
    }

    //合并分块
    public RspResult mergeChunks(String fileMd5, String fileName, Long fileSize, String mimetype, String fileExt) {
        RspResult result = new RspResult();
        boolean b = minioUtil.merge(fileMd5, bucketName, fileName);
        if (!b) {
            //合并失败
            result.setFlag(ResultMessage.FALSE);
            result.setMessage(ResultMessage.MERGE_FAIL);
            return result;
        }
        //校验md5
        boolean checkMd5 = checkMd5(bucketName, fileName, fileMd5);
        if (!checkMd5) {
            //删除合并的文件
            minioUtil.delete(bucketName, fileName);
            result.setFlag(ResultMessage.FALSE);
            result.setMessage(ResultMessage.MD5_CHECK_FAIL);
            return result;
        }
        //将md5存入redis
        redisTemplate.opsForValue().set(fileMd5, fileMd5);
        //删除临时桶
        boolean removeBucket = minioUtil.removeBucket(fileMd5);
        if (!removeBucket) {
            result.setFlag(ResultMessage.FALSE);
            result.setMessage(ResultMessage.DELETE_BUCKET_FAIL);
            return result;
        }
        result.setFlag(ResultMessage.TRUE);
        result.setMessage(ResultMessage.MERGE_SUCCESS);
        return result;
    }

    //下载
    public void download(HttpServletResponse response, String bucketName, String filename) {
        minioUtil.download(response, bucketName, filename);
    }

    //删除
    public RspResult delete(String bucketName, String filename) {
        RspResult result = new RspResult();
        boolean delete = minioUtil.delete(bucketName, filename);
        if (!delete) {
            result.setFlag(ResultMessage.FALSE);
            result.setMessage(ResultMessage.DELETE_FAIL);
            return result;
        }
        result.setFlag(ResultMessage.TRUE);
        result.setMessage(ResultMessage.DELETE_SUCCESS);
        return result;
    }


    //预览
    public void preview(HttpServletResponse response, String bucketName, String filename) throws Exception {
        //获取文件输入流
        InputStream inputStream = minioUtil.getInput(bucketName, filename);
        //获取文件信息
        StatObjectResponse statObject = minioUtil.getStatObject(bucketName, filename);
        // 设置相关格式
        response.setContentType(statObject.contentType());
        response.setHeader("Connection", "keep-alive");  //保持长连接. http1.1 默认支持

        IOUtils.copy(inputStream, response.getOutputStream());

        inputStream.close();


    }


    public List<String> getFileNames(List<Object> folderList) {
        List fileNames = new ArrayList<>();
        if (folderList != null && !folderList.isEmpty()) {
            for (int i = 0; i < folderList.size(); i++) {
                Map o = (Map) folderList.get(i);
                String name = (String) o.get("fileName");
                fileNames.add(name);
            }
        }
        return fileNames;
    }

    private boolean checkMd5(String bucketName, String fileName, String md5) {
        try {
            //利用apache工具类获取文件md5值
            InputStream inputStream = minioUtil.getInput(bucketName, fileName);
            String md5Hex = DigestUtils.md5Hex(inputStream);
            if (md5.equalsIgnoreCase(md5Hex)) {
                return ResultMessage.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.FALSE;
    }
}
