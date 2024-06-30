package com.a.b.c.d.pangolin.util.jwt;

import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public abstract class AbstractJwtHandler<T> implements JwtHandler<T> {
    @Override
    public String serialize(JWSSigner signer, JWSHeader header, JWTClaimsSet claims) throws Exception {
        SignedJWT signedJWT = new SignedJWT(header, claims);
        signedJWT.sign(signer);
        return signedJWT.serialize();
    }

    @Override
    public JWTClaimsSet deserialize(String jwt) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        return signedJWT.getJWTClaimsSet();
    }

    @Override
    public boolean verify(String jwt, JWSVerifier verifier) throws Exception {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        return signedJWT.verify(verifier);
    }
}
