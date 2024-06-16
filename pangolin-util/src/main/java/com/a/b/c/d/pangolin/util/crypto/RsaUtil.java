package com.a.b.c.d.pangolin.util.crypto;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * <pre>
 *     密钥长度不限制，默认长度1024位，推荐长度2048位
 * </pre>
 */
public class RsaUtil {
    private RsaUtil() {
    }

    //标准签名算法名称
    private static final String RSA_SIGNATURE_ALGORITHM = "SHA1withRSA";
    private static final String RSA2_SIGNATURE_ALGORITHM = "SHA256withRSA";

    public static KeyPair genKeyPair(int byteLen, byte[] seed) throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
        if (ArrayUtils.isNotEmpty(seed)) {
            SecureRandom random = new SecureRandom();
            random.setSeed(seed);
            kpg.initialize(byteLen * 8, random);
        } else {
            kpg.initialize(byteLen * 8);
        }
        return kpg.genKeyPair();
    }

    /**
     * 公钥加密(用于数据加密)
     *
     * @param input  加密前的数据
     * @param pubKey 公钥
     * @throws Exception
     */
    public static byte[] encrypt(byte[] input, byte[] pubKey) throws Exception {
        //创建X509编码密钥规范
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        //返回转换指定算法的KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //根据X509编码密钥规范产生公钥对象
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //用公钥初始化此Cipher对象（加密模式）
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        //对数据加密
        return cipher.doFinal(input);
    }

    /**
     * 私钥解密(用于数据解密)
     *
     * @param input  解密前的数据
     * @param priKey 私钥
     * @throws Exception
     */
    public static byte[] decrypt(byte[] input, byte[] priKey) throws Exception {
        //创建PKCS8编码密钥规范
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        //返回转换指定算法的KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //根据PKCS8编码密钥规范产生私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //根据转换的名称获取密码对象Cipher（转换的名称：算法/工作模式/填充模式）
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        //用私钥初始化此Cipher对象（解密模式）
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        //对数据解密
        return cipher.doFinal(input);
    }

    /**
     * RSA签名(用私钥签名)
     *
     * @param data     待签名数据
     * @param priKey   私钥
     * @param signType RSA或RSA2
     * @return 签名
     * @throws Exception
     */
    public static byte[] sign(byte[] data, byte[] priKey, RsaSignTypeEnum signType) throws Exception {
        //创建PKCS8编码密钥规范
        PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(priKey);
        //返回转换指定算法的KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //根据PKCS8编码密钥规范产生私钥对象
        PrivateKey privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
        //标准签名算法名称(RSA还是RSA2)
        String algorithm = ObjectUtils.defaultIfNull(signType, RsaSignTypeEnum.RSA_SIGNATURE_ALGORITHM).getCode();
        //用指定算法产生签名对象Signature
        Signature signature = Signature.getInstance(algorithm);
        //用私钥初始化签名对象Signature
        signature.initSign(privateKey);
        //将待签名的数据传送给签名对象(须在初始化之后)
        signature.update(data);
        //返回签名结果字节数组
        return signature.sign();
    }

    /**
     * RSA校验数字签名(用公钥验证签名)
     *
     * @param data     待校验数据
     * @param sign     数字签名
     * @param pubKey   公钥
     * @param signType RSA或RSA2
     * @return boolean 校验成功返回true，失败返回false
     */
    public static boolean verify(byte[] data, byte[] sign, byte[] pubKey, RsaSignTypeEnum signType) throws Exception {
        //返回转换指定算法的KeyFactory对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //创建X509编码密钥规范
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(pubKey);
        //根据X509编码密钥规范产生公钥对象
        PublicKey publicKey = keyFactory.generatePublic(x509KeySpec);
        //标准签名算法名称(RSA还是RSA2)
        String algorithm = ObjectUtils.defaultIfNull(signType, RsaSignTypeEnum.RSA_SIGNATURE_ALGORITHM).getCode();
        //用指定算法产生签名对象Signature
        Signature signature = Signature.getInstance(algorithm);
        //用公钥初始化签名对象,用于验证签名
        signature.initVerify(publicKey);
        //更新签名内容
        signature.update(data);
        //得到验证结果
        return signature.verify(sign);
    }


    public static enum KeyLenEnum {
        LEN_64(64),
        LEN_128(128),
        LEN_512(512),
        LEN_1024(1024),
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

    public static enum RsaSignTypeEnum {
        RSA_SIGNATURE_ALGORITHM("SHA1withRSA"),
        RSA2_SIGNATURE_ALGORITHM("SHA256withRSA"),
        ;
        private String code;

        RsaSignTypeEnum(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }
}
