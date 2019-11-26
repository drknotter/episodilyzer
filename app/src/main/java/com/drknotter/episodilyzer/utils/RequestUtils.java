package com.drknotter.episodilyzer.utils;

public class RequestUtils {
    public static String getBearerString(String authToken) {
        return "Bearer " + authToken;
    }
}
