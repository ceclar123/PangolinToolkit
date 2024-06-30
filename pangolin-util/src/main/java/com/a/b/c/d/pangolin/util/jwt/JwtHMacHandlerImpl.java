package com.a.b.c.d.pangolin.util.jwt;

import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;

public class JwtHMacHandlerImpl extends AbstractJwtHandler<String> implements JwtHandler<String> {
    @Override
    public Class getKeyType() {
        return String.class;
    }

    @Override
    public boolean support(JWSAlgorithm algorithm) {
        return JWSAlgorithm.HS256.equals(algorithm) || JWSAlgorithm.HS384.equals(algorithm) || JWSAlgorithm.HS512.equals(algorithm);
    }

    @Override
    public JwtKeyDTO genKey(JWSAlgorithm algorithm) throws Exception {
        int len = MACSigner.getMinRequiredSecretLength(algorithm);
        String secret = StringUtil.getRandomString(len);
        return new JwtKeyDTO(secret, null);
    }

    @Override
    public JWSSigner buildSigner(String key) throws Exception {
        return new MACSigner(key);
    }

    @Override
    public JWSVerifier buildVerifier(String key) throws Exception {
        return new MACVerifier(key);
    }
}
