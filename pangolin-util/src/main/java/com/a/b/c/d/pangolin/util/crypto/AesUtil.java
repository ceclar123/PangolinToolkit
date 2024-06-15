package com.a.b.c.d.pangolin.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AesUtil {
    /**
     * 加密模式之 ECB，算法/模式/补码方式
     * <pre>
     *     在ECB模式中，每一个块独立加密，相同的明文块会被加密成相同的密文块。这带来的一个主要缺点是，它不能很好地隐藏数据模式。因此，对于大多数应用而言，ECB模式不推荐使用
     *     1、密钥长度128位、192位、256位
     * </pre>
     */
    public static final String AES_ECB = "AES/ECB/PKCS5Padding";

    /**
     * 加密模式之 CBC，算法/模式/补码方式
     * <pre>
     *     CBC模式是一种操作模式，它使用一个固定长度的初始向量（IV），并要求每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或。这种方法增加了加密数据的复杂性和安全性，因为相同的明文块不会被加密为相同的密文块。
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final String AES_CBC = "AES/CBC/PKCS5Padding";

    /**
     * 加密模式之 CFB，算法/模式/补码方式
     * <pre>
     *     CFB模式将AES变成一种流密码，使得它可以在加密数据时不需要填充（padding），因为它可以工作在任何长度的数据上。此外，它在加密每个比特或者字节前，利用上一个步骤的加密结果作为反馈。
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final String AES_CFB = "AES/CFB/PKCS5Padding";
    /**
     * AES 中的 IV 必须是 16 字节（128位）长
     */
    public static final Integer IV_LENGTH = 16;

    public static final String BASE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static final List<String> MODE_LIST = Arrays.asList(
            "AES/CBC/NoPadding",
            "AES/CBC/PKCS5Padding",
            "AES/CBC/ISO10126Padding",
            "AES/CFB/NoPadding",
            "AES/CFB/PKCS5Padding",
            "AES/CFB/ISO10126Padding",
            "AES/ECB/NoPadding",
            "AES/ECB/PKCS5Padding",
            "AES/ECB/ISO10126Padding",
            "AES/OFB/NoPadding",
            "AES/OFB/PKCS5Padding",
            "AES/OFB/ISO10126Padding",
            "AES/PCBC/NoPadding",
            "AES/PCBC/PKCS5Padding",
            "AES/PCBC/ISO10126Padding",
            "AES/CTR/NoPadding",
            "AES/CTR/PKCS5Padding",
            "AES/CTR/ISO10126Padding"
    );

    private AesUtil() {
    }

    public static byte[] encryptECB(byte[] input, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ECB);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(input);
    }

    public static byte[] decryptECB(byte[] input, byte[] key) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ECB);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        return cipher.doFinal(input);
    }

    public static byte[] encryptCBC(byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(input);
    }

    public static byte[] decryptCBC(byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CBC);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(input);
    }

    public static byte[] encryptCFB(byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CFB);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(input);
    }

    public static byte[] decryptCFB(byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_CFB);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        return cipher.doFinal(input);
    }

    public static byte[] encrypt(String mode, byte[] input, byte[] key, byte[] iv) throws Exception {
        byte[] data;
        switch (mode) {
            case AES_ECB:
                data = encryptECB(input, key);
                break;
            case AES_CBC:
                data = encryptCBC(input, key, iv);
                break;
            case AES_CFB:
                data = encryptCFB(input, key, iv);
                break;
            default:
                data = new byte[0];
                break;
        }

        return data;
    }

    public static byte[] decrypt(String mode, byte[] input, byte[] key, byte[] iv) throws Exception {
        byte[] data;
        switch (mode) {
            case AES_ECB:
                data = decryptECB(input, key);
                break;
            case AES_CBC:
                data = decryptCBC(input, key, iv);
                break;
            case AES_CFB:
                data = decryptCFB(input, key, iv);
                break;
            default:
                data = new byte[0];
                break;
        }

        return data;
    }


    public static String getString(int len) {
        int size = BASE_STRING.length();
        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int ix = random.nextInt(0, size);
            builder.append(BASE_STRING.charAt(ix));
        }
        return builder.toString();
    }


    public static enum KeyLenEnum {
        LEN_16(16),
        LEN_24(24),
        LEN_32(32),
        ;

        /**
         * 字节数
         */
        private int len;

        KeyLenEnum(int len) {
            this.len = len;
        }

        public int getLen() {
            return len;
        }

        private void setLen(int len) {
            this.len = len;
        }
    }
}
