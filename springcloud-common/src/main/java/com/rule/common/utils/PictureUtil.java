package com.rule.common.utils;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;

public class PictureUtil {
    public static String request(String httpUrl, String httpArg) {
        BufferedReader reader = null;
        String result = null;
        StringBuilder sbf = new StringBuilder();
        try {
            // 用java JDK自带的URL去请求
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            // 设置该请求的消息头
            // 设置HTTP方法：POST
            connection.setRequestMethod("POST");
            // 设置其Header的Content-Type参数为application/x-www-form-urlencoded
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            // 填入apikey到HTTP header
            connection.setRequestProperty("apikey", "uml8HFzu2hFd8iEG2LkQGMxm");
            // 将第二步获取到的token填入到HTTP header
            connection.setRequestProperty("access_token", BaiduOCR.getAuth());
            connection.setDoOutput(true);
            connection.getOutputStream().write(httpArg.getBytes(StandardCharsets.UTF_8));
            connection.connect();
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String strRead;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 把json格式转换成HashMap
    public static HashMap<String, String> getHashMapByJson(String jsonResult) {
        HashMap<String, String> map = new HashMap<>();
        try {
            JSONObject jsonObject = JSONObject.parseObject(jsonResult.toString());
            JSONObject wordsResult = jsonObject.getJSONObject("words_result");
            System.out.println(wordsResult);
            /*for (String key : words_result.keySet()) {
                JSONObject result = words_result.getJSONObject(key);
                String value = result.getString("words");
                switch (key) {
                    case "姓名":
                        map.put("name", value);
                        break;
                    case "民族":
                        map.put("nation", value);
                        break;
                    case "住址":
                        map.put("address", value);
                        break;
                    case "公民身份号码":
                        map.put("IDCard", value);
                        break;
                    case "出生":
                        map.put("Birth", value);
                        break;
                    case "性别":
                        map.put("sex", value);
                        break;
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        requestCommonPicture();
    }

    public static void requestIdCardPicture() {

        // 获取本地的绝对路径图片
        InputStream inputStream = PictureUtil.class.getResourceAsStream("/idCard.png");
//        FileInputStream file = new FileInputStream(inputStream);
        // 进行BASE64位编码
        String imageBase = Utils.encodeImgageToBase64(inputStream);
        imageBase = imageBase.replaceAll("\r\n", "");
        imageBase = imageBase.replaceAll("\\+", "%2B");
        // 百度云的文字识别接口,后面参数为获取到的token
        String httpUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/idcard?access_token=" + BaiduOCR.getAuth();
        String httpArg = "detect_direction=true&id_card_side=front&image=" + imageBase;
        String jsonResult = request(httpUrl, httpArg);
        System.out.println("返回的结果--------->" + jsonResult);
        HashMap<String, String> map = getHashMapByJson(jsonResult);
        Collection<String> values = map.values();
        for (String value : values) {
            System.out.print(value + ", ");
        }
    }



    public static void requestCommonPicture() {

        InputStream inputStream = PictureUtil.class.getResourceAsStream("/2.png");
        String imageBase = Utils.encodeImgageToBase64(inputStream);
        imageBase = imageBase.replaceAll("\r\n", "");
        imageBase = imageBase.replaceAll("\\+", "%2B");
        String httpUrl = "https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic?access_token=" + BaiduOCR.getAuth();
        String httpArg = "detect_direction=true&paragraph=true&probability=true&image=" + imageBase;
        String jsonResult = request(httpUrl, httpArg);
        System.out.println(jsonResult);
    }

}
