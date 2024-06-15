package com.a.b.c.d.pangolin.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AesUtil {
    /**
     * AES 中的 IV 必须是 16 字节（128位）长
     */
    public static final Integer IV_LENGTH_12 = 12;
    public static final Integer IV_LENGTH_16 = 16;

    public static final String BASE_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * CBC模式，每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或
     * <pre>
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_CBC = Arrays.asList("AES/CBC/NoPadding", "AES/CBC/PKCS5Padding", "AES/CBC/ISO10126Padding");
    /**
     * CFB模式，每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或
     * <pre>
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_CFB = Arrays.asList("AES/CFB/NoPadding", "AES/CFB/PKCS5Padding", "AES/CFB/ISO10126Padding");
    /**
     * ECB模式，明文被分割成相同大小(通常16字节)的快，每个块用相同的密钥进行加密
     * <pre>     *
     *     1、密钥长度128位、192位、256位
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_ECB = Arrays.asList("AES/ECB/NoPadding", "AES/ECB/PKCS5Padding", "AES/ECB/ISO10126Padding");
    /**
     * OFB模式，不直接加密数据，而是生成密钥流，密钥流与明文数据进行异或操作生成密文
     * <pre>
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_OFB = Arrays.asList("AES/OFB/NoPadding", "AES/OFB/PKCS5Padding", "AES/OFB/ISO10126Padding");
    /**
     * PCBC模式，每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或，同时当前密文计算的时候还会加入当前明文的值，由于其复杂的错误传播特性，在实际中很少使用，推荐使用CBC、CTR等模式
     * <pre>
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_PCBC = Arrays.asList("AES/PCBC/NoPadding", "AES/PCBC/PKCS5Padding", "AES/PCBC/ISO10126Padding");
    /**
     * CTR模式，通过将一个计数器与初始向量IV相结合，并使用AES算法对这个结合加密，生成密钥流，将密钥流与明文进行异或操作得到密文
     * <pre>
     *     1、密钥长度128位、192位、256位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_CTR = Arrays.asList("AES/CTR/NoPadding", "AES/CTR/PKCS5Padding", "AES/CTR/ISO10126Padding");
    /**
     * GCM模式，结合了加密与认证，既提供了保密性，又提供了完整性，先生成密钥流(类似CTR模式)，同时利用Galois域乘法和一个散列函数来生成一个认证tag，利用这个tag来验证数据完整性与真实性
     * <pre>
     *     1、密钥长度128位、192位、256位（也是tag长度）
     *     2、初始向量12字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_GCM = Arrays.asList("AES/GCM/NoPadding", "AES/GCM/PKCS5Padding", "AES/GCM/ISO10126Padding");

    public static final List<String> SYS_MODE_LIST = Stream.of(SYS_MODE_LIST_CBC, SYS_MODE_LIST_CFB, SYS_MODE_LIST_ECB, SYS_MODE_LIST_OFB, SYS_MODE_LIST_PCBC, SYS_MODE_LIST_CTR, SYS_MODE_LIST_GCM)
            .flatMap(Collection::stream)
            .collect(Collectors.toList());

    private AesUtil() {
    }

    public static byte[] encrypt(String mode, byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        if (SYS_MODE_LIST_ECB.contains(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        } else if (SYS_MODE_LIST_GCM.contains(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(key.length * 8, iv));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        }
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(String mode, byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        if (SYS_MODE_LIST_ECB.contains(mode)) {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        } else if (SYS_MODE_LIST_GCM.contains(mode)) {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new GCMParameterSpec(key.length * 8, iv));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
        }
        return cipher.doFinal(input);
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
