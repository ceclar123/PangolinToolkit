package com.a.b.c.d.pangolin.util.crypto;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

/***
 * <pre>
 *     JWT令牌由三个部分组成，分别是标头（Header）、有效载荷（Payload）、签名（Signature），并且由 "." 分割，类似于 xxxx.yyyy.zzzzz ，也就是 Header.Payload.Signature
 *     1、Header，标头通常由两个部分组成，分别是令牌的类型（例如 JWT）和所使用的签名算法.（例如HMAC、SHA256、RSA.  一般就是用HS256即可），Base64编码，例如：{"alg": "HS256","typ": "JWT"}
 *     2、Payload，这就是我们一些自定义传输的信息，Base64编码，例如：{"id": "6","name": "hello","age": 10}
 *     3、Signature，需要使用Base64编码后的Header和Payload以及提供的密钥（私钥），然后使用Header中指定的签名算法（HS256）构建一个签名，保证JWT没有被篡改过
 * </pre>
 */
public class JwtUtil {

    public static JWSSigner buildSigner(JWSAlgorithm algorithm, byte[] secret) throws KeyLengthException {
        return new MACSigner(secret);
    }

    public static JWSVerifier buildVerifier(JWSAlgorithm algorithm, byte[] secret) throws JOSEException {
        return new MACVerifier(secret);
    }

    public static JWSHeader buildHeader(JWSAlgorithm algorithm) {
        return new JWSHeader(algorithm);
    }

    public static JWTClaimsSet buildClaims(long beforeTimeMs, long expireTimeMs) {
        return new JWTClaimsSet.Builder()
                .issuer("pangolin")//签发人
                .issueTime(new Date())//签发时间
                .notBeforeTime(new Date(beforeTimeMs))//生效时间
                .expirationTime(new Date(expireTimeMs))//过期时间
                .subject("PangolinToolkit")//主题
                .audience("")//受众
                .jwtID(UUID.randomUUID().toString())//JWT编号
                .build();
    }

    public static String serialize(JWSSigner signer, JWSHeader header, JWTClaimsSet claims) throws JOSEException {
        SignedJWT signedJWT = new SignedJWT(header, claims);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    public static boolean serialize(String jwt, JWSVerifier verifier) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        return signedJWT.verify(verifier);
    }

    public static JWTClaimsSet deserialize(String jwt) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        return signedJWT.getJWTClaimsSet();
    }
}
