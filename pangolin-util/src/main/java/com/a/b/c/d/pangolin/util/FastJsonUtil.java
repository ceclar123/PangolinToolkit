package com.a.b.c.d.pangolin.util;

import com.alibaba.fastjson2.*;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public class FastJsonUtil {
    private FastJsonUtil() {
    }

    public static boolean checkValid(String text) {
        return JSONValidator.from(text).validate();
    }

    public static String formatCompress(String text) throws JsonProcessingException {
        if (JSON.isValidObject(text)) {
            JSONObject jo = JSON.parseObject(text);
            return JSON.toJSONString(jo);
        } else if (JSON.isValidArray(text)) {
            JSONArray ja = JSON.parseArray(text);
            return JSON.toJSONString(ja);
        } else {
            throw new JSONException("JSON内容非法");
        }
    }

    public static String formatPretty(String text) throws JsonProcessingException {
        if (JSON.isValidObject(text)) {
            JSONObject jo = JSON.parseObject(text);
            return JSON.toJSONString(jo, JSONWriter.Feature.PrettyFormat);
        } else if (JSON.isValidArray(text)) {
            JSONArray ja = JSON.parseArray(text);
            return JSON.toJSONString(ja, JSONWriter.Feature.PrettyFormat);
        } else {
            throw new JSONException("JSON内容非法");
        }
    }

    public static String formatAddEscape(String text) throws JsonProcessingException {
        return JSON.toJSONString(text);
    }

    public static String formatRemoveEscape(String text) throws JsonProcessingException {
        return JSON.parse(text).toString();
    }


    public static <T> List<T> parseArray(String text, Class<T> type) {
        return JSON.parseArray(text, type);
    }
}
