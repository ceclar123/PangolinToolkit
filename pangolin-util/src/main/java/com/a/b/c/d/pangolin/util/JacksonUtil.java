package com.a.b.c.d.pangolin.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class JacksonUtil {
    private JacksonUtil() {
    }

    public static String formatCompress(String text) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = objectMapper.readTree(text);
        return objectMapper.writeValueAsString(node);
    }

    public static String formatPretty(String text) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        JsonNode node = objectMapper.readTree(text);
        return objectMapper.writeValueAsString(node);
    }
}
