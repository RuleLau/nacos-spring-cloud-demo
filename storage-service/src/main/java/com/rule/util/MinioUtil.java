package com.rule.util;


import com.alibaba.fastjson.JSON;
import com.rule.common.ResultMessage;
import io.minio.BucketExistsArgs;
import io.minio.ComposeObjectArgs;
import io.minio.ComposeSource;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.ListObjectsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveBucketArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Result;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * minio常用操作
 */
@Component
public class MinioUtil {
    @Autowired
    private MinioClient minioClient;

    //获取文件流
    public InputStream getInput(String bucketName, String filename) throws Exception {
        return minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filename).build());
    }

    //获取minio指定文件对象信息
    public StatObjectResponse getStatObject(String bucketName, String filename) throws Exception {
        return minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucketName)
                .object(filename)
                .build());
    }

    //获得

    //删除桶 --(不是空桶也删)
    public boolean removeBucket(String bucketName) {
        try {
            List<Object> folderList = this.getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (folderList != null && !folderList.isEmpty()) {
                for (Object value : folderList) {
                    Map o = (Map) value;
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            if (!fileNames.isEmpty()) {
                for (String fileName : fileNames) {
                    this.delete(bucketName, fileName);
                }
            }
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            return ResultMessage.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.FALSE;
    }

    //文件上传
    public boolean upload(String bucketName, MultipartFile file, String fileName) {
        //返回客户端文件系统中的原始文件名
        String originalFilename = file.getOriginalFilename();
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .stream(inputStream, file.getSize(), -1)
                    .build());
            return ResultMessage.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return ResultMessage.FALSE;
    }

    //文件下载
    public void download(HttpServletResponse response, String bucketName, String fileName) {
        InputStream inputStream = null;
        try {
            //获取文件源信息
            StatObjectResponse statObject = minioClient.statObject(StatObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build());
            //设置响应的内容类型 --浏览器对应不同类型做不同处理
            response.setContentType(statObject.contentType());
            //设置响应头
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode(fileName, "UTF-8"));

            //文件下载
            inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName)
                    .object(fileName).build());
            //利用apache的IOUtils
            IOUtils.copy(inputStream, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //文件删除
    public boolean delete(String bucketName, String fileName) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(bucketName)
                    .object(fileName).build());
            return ResultMessage.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.FALSE;
    }

    //桶是否存在
    public boolean bucketExists(String bucketName) {
        boolean b = false;
        try {
            b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (b) {
                return ResultMessage.TRUE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.FALSE;
    }

    //创建桶
    public boolean createBucket(String bucketName) {
        try {
            boolean b = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!b) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
            return ResultMessage.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.FALSE;
    }

    //获取桶列表
    public List getBucketList() throws Exception {
        List<Bucket> buckets = minioClient.listBuckets();
        List list = new ArrayList();
        for (Bucket bucket : buckets) {
            String name = bucket.name();
            list.add(name);
        }
        return list;
    }

    //获取指定bucketName下所有文件 文件名+大小
    public List<Object> getFolderList(String bucketName) throws Exception {
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder().bucket(bucketName).build());
        Iterator<Result<Item>> iterator = results.iterator();
        List<Object> items = new ArrayList<>();
        String format = "{'fileName':'%s','fileSize':'%s'}";
        while (iterator.hasNext()) {
            Item item = iterator.next().get();
            items.add(JSON.parse((String.format(format, item.objectName(),
                    formatFileSize(item.size())))));
        }
        return items;
    }

    /**
     * 讲快文件合并到新桶   块文件必须满足 名字是 0 1  2 3 5....
     *
     * @param bucketName  存块文件的桶
     * @param bucketName1 存新文件的桶
     * @param fileName1   存到新桶中的文件名称
     * @return
     */
    public boolean merge(String bucketName, String bucketName1, String fileName1) {
        try {
            List<ComposeSource> sourceObjectList = new ArrayList<ComposeSource>();
            List<Object> folderList = this.getFolderList(bucketName);
            List<String> fileNames = new ArrayList<>();
            if (folderList != null && !folderList.isEmpty()) {
                for (int i = 0; i < folderList.size(); i++) {
                    Map o = (Map) folderList.get(i);
                    String name = (String) o.get("fileName");
                    fileNames.add(name);
                }
            }
            if (!fileNames.isEmpty()) {
                Collections.sort(fileNames, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        if (Integer.parseInt(o2) > Integer.parseInt(o1)) {
                            return -1;
                        }
                        return 1;
                    }
                });
                for (String name : fileNames) {
                    sourceObjectList.add(ComposeSource.builder().bucket(bucketName).object(name).build());
                }
            }

            minioClient.composeObject(
                    ComposeObjectArgs.builder()
                            .bucket(bucketName1)
                            .object(fileName1)
                            .sources(sourceObjectList)
                            .build());
            return ResultMessage.TRUE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.FALSE;
    }

    //获得minio文件下载地址 --->>  旧版为预览地址
    public String downloadNew(String bucketName, String filename) {
        try {
            String getUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .method(Method.GET)
                    .bucket(bucketName)
                    .object(filename)
                    .expiry(2, TimeUnit.HOURS)
                    .build());
            return getUrl;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResultMessage.GET_URL_FAIL;
    }

    //预览
    public void preview(HttpServletResponse response, String bucketName, String filename) {
        try {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder().bucket(bucketName).object(filename).build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + " B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + " KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + " MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + " GB";
        }
        return fileSizeString;
    }


}
