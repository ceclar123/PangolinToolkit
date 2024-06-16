package com.a.b.c.d.pangolin.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Arrays;
import java.util.List;

public class TripleDesUtil {
    /**
     * DESede 中的 IV 必须是 8 字节（64位）长
     */
    public static final Integer IV_LENGTH_8 = 8;

    /**
     * CBC模式，每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或
     * <pre>
     *     1、密钥长度56位，剩余8位为奇偶校验位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_CBC = Arrays.asList("DESede/CBC/NoPadding", "DESede/CBC/PKCS5Padding", "DESede/CBC/ISO10126Padding");
    /**
     * CFB模式，每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或
     * <pre>
     *     1、密钥长度56位，剩余8位为奇偶校验位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_CFB = Arrays.asList("DESede/CFB/NoPadding", "DESede/CFB/PKCS5Padding", "DESede/CFB/ISO10126Padding");
    /**
     * ECB模式，明文被分割成相同大小(通常16字节)的快，每个块用相同的密钥进行加密
     * <pre>     *
     *     1、密钥长度56位，剩余8位为奇偶校验位
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_ECB = Arrays.asList("DESede/ECB/NoPadding", "DESede/ECB/PKCS5Padding", "DESede/ECB/ISO10126Padding");
    /**
     * OFB模式，不直接加密数据，而是生成密钥流，密钥流与明文数据进行异或操作生成密文
     * <pre>
     *     1、密钥长度56位，剩余8位为奇偶校验位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_OFB = Arrays.asList("DESede/OFB/NoPadding", "DESede/OFB/PKCS5Padding", "DESede/OFB/ISO10126Padding");
    /**
     * PCBC模式，每个明文块在加密之前与前一个密文块的输出进行异或操作。第一个明文块与初始向量进行异或，同时当前密文计算的时候还会加入当前明文的值，由于其复杂的错误传播特性，在实际中很少使用，推荐使用CBC、CTR等模式
     * <pre>
     *     1、密钥长度56位，剩余8位为奇偶校验位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_PCBC = Arrays.asList("DESede/PCBC/NoPadding", "DESede/PCBC/PKCS5Padding", "DESede/PCBC/ISO10126Padding");
    /**
     * CTR模式，通过将一个计数器与初始向量IV相结合，并使用DESede算法对这个结合加密，生成密钥流，将密钥流与明文进行异或操作得到密文
     * <pre>
     *     1、密钥长度56位，剩余8位为奇偶校验位
     *     2、初始向量16字节
     * </pre>
     */
    public static final List<String> SYS_MODE_LIST_CTR = Arrays.asList("DESede/CTR/NoPadding", "DESede/CTR/PKCS5Padding", "DESede/CTR/ISO10126Padding");


    private TripleDesUtil() {
    }

    public static byte[] encrypt(String mode, byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        if (SYS_MODE_LIST_ECB.contains(mode)) {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"));
        } else {
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DESede"), new IvParameterSpec(iv));
        }
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(String mode, byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(mode);
        if (SYS_MODE_LIST_ECB.contains(mode)) {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"));
        } else {
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DESede"), new IvParameterSpec(iv));
        }
        return cipher.doFinal(input);
    }

    public static enum KeyLenEnum {
        LEN_24(24),
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
