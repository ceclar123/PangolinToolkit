package com.a.b.c.d.pangolin.util.jwt;

import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.KeyUse;
import com.nimbusds.jose.jwk.gen.ECKeyGenerator;

public class JwtEcHandlerImpl extends AbstractJwtHandler<ECKey> implements JwtHandler<ECKey> {
    @Override
    public Class getKeyType() {
        return ECKey.class;
    }

    @Override
    public boolean support(JWSAlgorithm algorithm) {
        return JWSAlgorithm.ES256.equals(algorithm) || JWSAlgorithm.ES384.equals(algorithm) || JWSAlgorithm.ES512.equals(algorithm) || JWSAlgorithm.ES256K.equals(algorithm);
    }

    @Override
    public JwtKeyDTO genKey(JWSAlgorithm algorithm) throws Exception {
        if (JWSAlgorithm.ES256.equals(algorithm)) {
            return new JwtKeyDTO(null, new ECKeyGenerator(Curve.P_256).generate());
        } else if (JWSAlgorithm.ES384.equals(algorithm)) {
            return new JwtKeyDTO(null, new ECKeyGenerator(Curve.P_384).generate());
        } else if (JWSAlgorithm.ES512.equals(algorithm)) {
            return new JwtKeyDTO(null, new ECKeyGenerator(Curve.P_521).generate());
        } else if (JWSAlgorithm.ES256K.equals(algorithm)) {
            return new JwtKeyDTO(null, new ECKeyGenerator(Curve.SECP256K1).keyUse(KeyUse.SIGNATURE).generate());
        } else {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public JWSSigner buildSigner(ECKey key) throws Exception {
        return new ECDSASigner(key);
    }

    @Override
    public JWSVerifier buildVerifier(ECKey key) throws Exception {
        return new ECDSAVerifier(key);
    }
}
