package com.crawler.example.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONUtil {
    public static <T> T JSON2Object(Object beans,Class<T> obj) {
        T t=null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //允许使用未带引号的字段名
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            //允许使用单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            //允许使用反斜杠等转义字符
            objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            t=objectMapper.readValue(JSONObject.toJSONString(beans),obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public static <T> T JSON2Object(String jsonStr,Class<T> obj) {
        T t=null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //允许使用未带引号的字段名
            objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
            //允许使用单引号
            objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
            //允许使用反斜杠等转义字符
            objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
            t=objectMapper.readValue(jsonStr,obj);
        }catch (Exception e){
            e.printStackTrace();
        }
        return t;
    }

    public static void main(String[] args) {
    }
}
