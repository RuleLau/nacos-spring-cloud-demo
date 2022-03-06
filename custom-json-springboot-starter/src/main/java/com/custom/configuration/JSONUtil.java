package com.custom.configuration;

import com.alibaba.fastjson.JSONObject;

public class JSONUtil {

    public <T> String serialize(T t){
        return JSONObject.toJSONString(t);
    }
}
