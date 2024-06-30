package com.a.b.c.d.pangolin.util.jwt;

import com.a.b.c.d.pangolin.util.StringUtil;
import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;

public class JwtRsaHandlerImpl extends AbstractJwtHandler<RSAKey> implements JwtHandler<RSAKey> {
    @Override
    public Class getKeyType() {
        return RSAKey.class;
    }

    @Override
    public boolean support(JWSAlgorithm algorithm) {
        return JWSAlgorithm.RS256.equals(algorithm) || JWSAlgorithm.RS384.equals(algorithm) || JWSAlgorithm.RS512.equals(algorithm) ||
                JWSAlgorithm.PS256.equals(algorithm) || JWSAlgorithm.PS384.equals(algorithm) || JWSAlgorithm.PS512.equals(algorithm);
    }

    @Override
    public JwtKeyDTO genKey(JWSAlgorithm algorithm) throws Exception {
        int byteLen = StringUtil.getNumber(algorithm.getName());
        RSAKey key = new RSAKeyGenerator(byteLen * 8).generate();
        return new JwtKeyDTO(null, key);
    }

    @Override
    public JWSSigner buildSigner(RSAKey key) throws Exception {
        return new RSASSASigner(key);
    }

    @Override
    public JWSVerifier buildVerifier(RSAKey key) throws Exception {
        return new RSASSAVerifier(key.toRSAPublicKey());
    }
}
