package com.a.b.c.d.pangolin.util;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Random;

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
}
