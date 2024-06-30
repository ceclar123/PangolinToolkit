package com.a.b.c.d.pangolin.util.jwt;

import com.a.b.c.d.pangolin.util.bean.JwtKeyDTO;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.Ed25519Signer;
import com.nimbusds.jose.crypto.Ed25519Verifier;
import com.nimbusds.jose.jwk.Curve;
import com.nimbusds.jose.jwk.OctetKeyPair;
import com.nimbusds.jose.jwk.gen.OctetKeyPairGenerator;

public class JwtEdHandlerImpl extends AbstractJwtHandler<OctetKeyPair> implements JwtHandler<OctetKeyPair> {
    @Override
    public Class getKeyType() {
        return OctetKeyPair.class;
    }

    @Override
    public boolean support(JWSAlgorithm algorithm) {
        return JWSAlgorithm.EdDSA.equals(algorithm) || JWSAlgorithm.Ed25519.equals(algorithm) || JWSAlgorithm.Ed448.equals(algorithm);
    }

    @Override
    public JwtKeyDTO genKey(JWSAlgorithm algorithm) throws Exception {
        return new JwtKeyDTO(null, new OctetKeyPairGenerator(Curve.Ed25519).generate());
    }

    @Override
    public JWSSigner buildSigner(OctetKeyPair key) throws Exception {
        return new Ed25519Signer(key);
    }

    @Override
    public JWSVerifier buildVerifier(OctetKeyPair key) throws Exception {
        return new Ed25519Verifier(key);
    }
}
