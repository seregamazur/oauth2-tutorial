package com.seregamazur.oauth2.tutorial.verifier.oauth2;

import com.seregamazur.oauth2.tutorial.client.model.token.OAuth2TokenSet;

public interface OAuth2TokenVerifier {

    boolean verifyToken(String token);

    String verifyAndGetSubFromToken(OAuth2TokenSet tokenSet);

}
