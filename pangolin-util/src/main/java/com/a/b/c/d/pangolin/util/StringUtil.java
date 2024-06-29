package com.a.b.c.d.pangolin.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    private StringUtil() {
    }

    public static final String BASE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";


    public static String getRandomString(int len) {
        int size = BASE_STRING.length();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int ix = random.nextInt(0, size);
            builder.append(BASE_STRING.charAt(ix));
        }
        return builder.toString();
    }

    public static boolean isBlank(final CharSequence cs) {
        return StringUtils.isBlank(cs);
    }

    public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
        return StringUtils.defaultIfBlank(str, defaultStr);
    }

    public static String replace(final String text, final String searchString, final String replacement) {
        return StringUtils.replace(text, searchString, replacement);
    }

    /**
     * 字节数组转16进制
     *
     * @param bytes 需要转换的byte数组
     * @return 转换后的Hex字符串
     */
    public static String convertByteArrayToHexString(byte[] bytes) {
        if (ArrayUtils.isEmpty(bytes)) {
            return StringUtils.EMPTY;
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                builder.append(0);
            }
            builder.append(hex);
        }
        return builder.toString();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] convertHexStringToByteArray(String hexString) {
        if (StringUtils.isBlank(hexString)) {
            return new byte[0];
        }

        //合法性校验
        if (!hexString.matches("[a-fA-F0-9]*") || hexString.length() % 2 != 0) {
            return new byte[0];
        }

        int mid = hexString.length() / 2;
        byte[] bytes = new byte[mid];
        for (int i = 0; i < mid; i++) {
            bytes[i] = Integer.valueOf(hexString.substring(i * 2, i * 2 + 2), 16).byteValue();
        }

        return bytes;
    }

    public static String toLowerCamelCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        // 假设输入字符串是大驼峰，且每个单词间可能有下划线或空格，替换下划线为空格，并移除两端空格
        s = s.replaceAll("_", " ").trim();
        // 使用空格分割字符串
        String[] parts = s.split("\\s+");

        if (parts.length == 0) {
            return "";
        }

        StringBuilder camelCase = new StringBuilder();
        // 第一个单词首字母小写
        camelCase.append(parts[0].toLowerCase());
        for (int i = 1; i < parts.length; i++) {
            // 后续单词首字母大写
            camelCase.append(parts[i].substring(0, 1).toUpperCase());
            // 后续单词剩余部分小写
            camelCase.append(parts[i].substring(1).toLowerCase());
        }

        return camelCase.toString();
    }

    public static String toUpperCamelCase(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        // 假设输入字符串是小驼峰或包含下划线，将下划线后的字符前添加空格
        s = s.replaceAll("_(\\w)", " $1").trim();
        // 使用空格分割字符串
        String[] parts = s.split("\\s+");

        if (parts.length == 0) {
            return "";
        }

        StringBuilder camelCase = new StringBuilder();
        for (String part : parts) {
            // 每个单词首字母大写
            camelCase.append(part.substring(0, 1).toUpperCase());
            // 每个单词剩余部分小写
            camelCase.append(part.substring(1).toLowerCase());
        }

        // 第一个单词也需要大写（如果它是小驼峰）
        if (Character.isLowerCase(camelCase.charAt(0))) {
            camelCase.setCharAt(0, Character.toUpperCase(camelCase.charAt(0)));
        }

        return camelCase.toString();
    }

    public static int getNumber(String input) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return NumberUtils.toInt(matcher.group(), 0);
        }
        return 0;
    }
}
