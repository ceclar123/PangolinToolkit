package com.a.b.c.d.pangolin.util.jwt;

import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.nimbusds.jose.*;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class JwtFactoryUtil {
    private JwtFactoryUtil() {
    }

    private static final List<JwtHandler> handlerList = new ArrayList<JwtHandler>();

    static {
        handlerList.add(new JwtHMacHandlerImpl());
        handlerList.add(new JwtRsaHandlerImpl());
        handlerList.add(new JwtEcHandlerImpl());
        handlerList.add(new JwtEdHandlerImpl());
    }

    private static Optional<JwtHandler> getHandler(JWSAlgorithm algorithm) {
        for (JwtHandler handler : handlerList) {
            if (handler.support(algorithm)) {
                return Optional.ofNullable(handler);
            }
        }
        return Optional.empty();
    }

    /**
     * 生成密钥
     *
     * @param algorithm 算法
     * @return
     */
    public static JwtKeyDTO genKey(JWSAlgorithm algorithm) throws Exception {
        JwtHandler handler = getHandler(algorithm).orElse(null);
        Objects.requireNonNull(handler, "JwtHandler not found");

        return handler.genKey(algorithm);
    }

    /**
     * 构建JWSSigner
     *
     * @param key 密钥
     * @return
     * @throws KeyLengthException
     */
    public static JWSSigner buildSigner(JWSAlgorithm algorithm, JwtKeyDTO key) throws Exception {
        JwtHandler handler = getHandler(algorithm).orElse(null);
        Objects.requireNonNull(handler, "JwtHandler not found");

        if (handler.getKeyType() == String.class) {
            return handler.buildSigner(key.getSecret());
        } else {
            return handler.buildSigner(key.getJwkKey());
        }
    }

    /**
     * 构建JWSVerifier
     *
     * @param key 密钥
     * @return
     * @throws JOSEException
     */
    public static JWSVerifier buildVerifier(JWSAlgorithm algorithm, JwtKeyDTO key) throws Exception {
        JwtHandler handler = getHandler(algorithm).orElse(null);
        Objects.requireNonNull(handler, "JwtHandler not found");

        if (handler.getKeyType() == String.class) {
            return handler.buildVerifier(key.getSecret());
        } else {
            return handler.buildVerifier(key.getJwkKey());
        }
    }

    /**
     * 加密
     *
     * @param signer JWSSigner
     * @param header JWSHeader
     * @param claims JWTClaimsSet
     * @return
     * @throws JOSEException
     */
    public static String serialize(JWSAlgorithm algorithm, JWSSigner signer, JWSHeader header, JWTClaimsSet claims) throws Exception {
        JwtHandler handler = getHandler(algorithm).orElse(null);
        Objects.requireNonNull(handler, "JwtHandler not found");

        return handler.serialize(signer, header, claims);
    }

    /**
     * 解密
     *
     * @param jwt 密文
     * @return
     * @throws ParseException
     */
    public static JWTClaimsSet deserialize(JWSAlgorithm algorithm, String jwt) throws Exception {
        JwtHandler handler = getHandler(algorithm).orElse(null);
        Objects.requireNonNull(handler, "JwtHandler not found");

        return handler.deserialize(jwt);
    }

    /**
     * 验签
     *
     * @param jwt      密文
     * @param verifier JWSVerifier
     * @return
     * @throws ParseException
     * @throws JOSEException
     */
    public static boolean verify(JWSAlgorithm algorithm, String jwt, JWSVerifier verifier) throws Exception {
        JwtHandler handler = getHandler(algorithm).orElse(null);
        Objects.requireNonNull(handler, "JwtHandler not found");

        return handler.verify(jwt, verifier);
    }
}
