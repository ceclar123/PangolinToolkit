package com.a.b.c.d.pangolin.util.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class DesUtil {
    /**
     * 加密模式之 ECB，算法/模式/补码方式
     */
    private static final String DES_ECB = "DES/ECB/PKCS5Padding";

    /**
     * 加密模式之 CBC，算法/模式/补码方式
     */
    private static final String DES_CBC = "DES/CBC/PKCS5Padding";

    /**
     * 加密模式之 CFB，算法/模式/补码方式
     */
    private static final String DES_CFB = "DES/CFB/PKCS5Padding";
    /**
     * AES 中的 IV 必须是 8 字节（64位）长
     */
    private static final Integer IV_LENGTH = 8;

    private DesUtil() {
    }

    public static byte[] encrypt(byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(DES_CBC);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "DES"), new IvParameterSpec(iv));
        return cipher.doFinal(input);
    }

    public static byte[] decrypt(byte[] input, byte[] key, byte[] iv) throws Exception {
        Cipher cipher = Cipher.getInstance(DES_CBC);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "DES"), new IvParameterSpec(iv));
        return cipher.doFinal(input);
    }
}
