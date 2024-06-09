package com.a.b.c.d.pangolin.util;

import com.alibaba.fastjson2.JSON;

import java.util.List;

public class JsonUtil {
    private JsonUtil() {
    }

    public static <T> List<T> parseArray(String text, Class<T> type) {
        return JSON.parseArray(text, type);
    }
}
