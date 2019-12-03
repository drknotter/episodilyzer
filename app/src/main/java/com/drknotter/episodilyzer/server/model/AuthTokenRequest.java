package com.drknotter.episodilyzer.server.model;

public class AuthTokenRequest {
    final String apiKey;

    public AuthTokenRequest(String apiKey) {
        this.apiKey = apiKey;
    }
}
