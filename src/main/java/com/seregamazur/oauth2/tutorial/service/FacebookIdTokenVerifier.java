package com.seregamazur.oauth2.tutorial.service;

import org.springframework.stereotype.Service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Service
public class FacebookIdTokenVerifier {

    public static FacebookDecodedPayload verifyToken(String idToken) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(idToken);

            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            FacebookDecodedPayload payload = new FacebookDecodedPayload();
            payload.setIssuer(claimsSet.getIssuer());
            payload.setAudience(claimsSet.getAudience());
            payload.setSubject(claimsSet.getSubject());
            payload.setIssuedAt(claimsSet.getIssueTime().getTime());
            payload.setExpiration(claimsSet.getExpirationTime().getTime());
            payload.setJwtId(claimsSet.getJWTID());
            payload.setNonce(claimsSet.getStringClaim("nonce"));
            payload.setAccessTokenHash(claimsSet.getStringClaim("at_hash"));
            payload.setEmail(claimsSet.getStringClaim("email"));
            payload.setGivenName(claimsSet.getStringClaim("given_name"));
            payload.setFamilyName(claimsSet.getStringClaim("family_name"));
            payload.setName(claimsSet.getStringClaim("name"));
            payload.setPicture(claimsSet.getStringClaim("picture"));
            return payload;
        } catch (Exception e) {
            return null;
        }
    }
}
