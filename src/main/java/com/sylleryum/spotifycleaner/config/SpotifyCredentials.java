package com.sylleryum.spotifycleaner.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SpotifyCredentials {

//    @Value("${spotify.clientId}")
    private final String clientId;
//    @Value("${spotify.secretId}")
    private final String secretId;

    public SpotifyCredentials(@Value("${spotify.clientId}") String clientId, @Value("${spotify.secretId}") String secretId) {
        this.clientId = clientId;
        this.secretId = secretId;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSecretId() {
        return secretId;
    }
}
