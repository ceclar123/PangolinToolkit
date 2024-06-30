package com.a.b.c.d.pangolin.util.bean;

import com.nimbusds.jose.jwk.AsymmetricJWK;

import java.io.Serializable;

public class JwtKeyDTO implements Serializable {
    /**
     * 对称密钥
     */
    private String secret;

    /**
     * 非对称密钥
     */
    private AsymmetricJWK jwkKey;

    public JwtKeyDTO(String secret, AsymmetricJWK jwkKey) {
        this.secret = secret;
        this.jwkKey = jwkKey;
    }

    public String getSecret() {
        return secret;
    }

    public AsymmetricJWK getJwkKey() {
        return jwkKey;
    }
}
