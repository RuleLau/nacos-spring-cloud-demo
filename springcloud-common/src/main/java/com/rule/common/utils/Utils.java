package com.rule.common.utils;

import com.rule.common.annotation.Translate;
import com.rule.common.entity.UserInfo;
import sun.misc.BASE64Encoder;

import java.io.InputStream;
import java.lang.reflect.Field;

public class Utils {


    public static void doTranslate(Object obj) throws IllegalAccessException {
        if (obj == null) {
            return;
        }
//        UserInfo userInfo = new UserInfo();
        Class<?> p = obj.getClass();
        while (p != null) {
            for (Field field : p.getDeclaredFields()) {
                field.setAccessible(true);
                if (field.isAnnotationPresent(Translate.class)) {
                    // 记录信息 该field的信息，field， object
                    Translate annotation = field.getAnnotation(Translate.class);
                    System.out.println(annotation.code() + "===" + annotation.type());
                    continue;
                }
                Class<?> type = field.getType();
                if (type.isPrimitive()) {
                    continue;
                }
                // 非基础类型，包装类、list等
                doTranslate(field.get(obj));
            }
            p = p.getSuperclass();
        }
    }

    /**
     * 将本地图片进行Base64位编码
     *
     * @param imageFile 图片的url路径，如D:\\photo\\1.png
     * @return
     */
    public static String encodeImgageToBase64(InputStream in) {
        // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        // 其进行Base64编码处理
        byte[] data = null;
        // 读取图片字节数组
        try {
//            InputStream in = new FileInputStream(imageFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(data);// 返回Base64编码过的字节数组字符串
    }


    public static void main(String[] args) throws Exception {
        doTranslate(new UserInfo());
    }

}
