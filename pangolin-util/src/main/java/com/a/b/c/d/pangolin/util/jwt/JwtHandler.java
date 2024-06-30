package com.a.b.c.d.pangolin.util.jwt;

import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;

public interface JwtHandler<T> {
    /**
     * 密钥类型
     *
     * @return
     */
    Class getKeyType();

    /**
     * 支持算法
     *
     * @param algorithm 算法
     * @return
     */
    boolean support(JWSAlgorithm algorithm);

    /**
     * 生成密钥
     *
     * @param algorithm 算法
     * @return
     */
    JwtKeyDTO genKey(JWSAlgorithm algorithm) throws Exception;

    /**
     * 构建JWSSigner
     *
     * @param key 密钥
     * @return
     * @throws KeyLengthException
     */
    JWSSigner buildSigner(T key) throws Exception;

    /**
     * 构建JWSVerifier
     *
     * @param key 密钥
     * @return
     * @throws JOSEException
     */
    JWSVerifier buildVerifier(T key) throws Exception;

    /**
     * 加密
     *
     * @param signer JWSSigner
     * @param header JWSHeader
     * @param claims JWTClaimsSet
     * @return
     * @throws JOSEException
     */
    String serialize(JWSSigner signer, JWSHeader header, JWTClaimsSet claims) throws Exception;

    /**
     * 解密
     *
     * @param jwt 密文
     * @return
     * @throws ParseException
     */
    JWTClaimsSet deserialize(String jwt) throws Exception;

    /**
     * 验签
     *
     * @param jwt      密文
     * @param verifier JWSVerifier
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    boolean verify(String jwt, JWSVerifier verifier) throws Exception;
}
