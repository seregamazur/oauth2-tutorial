package com.seregamazur.oauth2.tutorial;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClient;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedClientId;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2AuthorizedData;
import com.seregamazur.oauth2.tutorial.client.model.OAuth2TokenProvider;

import lombok.experimental.UtilityClass;

@UtilityClass
public class TokensCache {

    private final Map<OAuth2AuthorizedClientId, OAuth2AuthorizedClient> CLIENT_TOKENS = new HashMap<>();

    public Collection<OAuth2AuthorizedClient> findAll() {
        return CLIENT_TOKENS.values();
    }

    public Optional<OAuth2AuthorizedClient> findByClientId(OAuth2TokenProvider clientId) {
        return CLIENT_TOKENS.entrySet()
            .stream()
            .filter(k -> k.getKey().getClientRegistrationId() == clientId)
            .map(Map.Entry::getValue)
            .findFirst();
    }

    public OAuth2AuthorizedClient add(OAuth2AuthorizedClientId clientId, OAuth2AuthorizedClient client) {
        return CLIENT_TOKENS.put(clientId, client);
    }

    public OAuth2AuthorizedClient add(OAuth2AuthorizedData authorizedData) {
        return CLIENT_TOKENS.put(authorizedData.getClientId(), authorizedData.getClient());
    }
}
