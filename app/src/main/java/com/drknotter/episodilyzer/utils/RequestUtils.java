package com.drknotter.episodilyzer.utils;

public class RequestUtils {
    public static String getBearerString() {
        return "Bearer " + PreferenceUtils.getAuthToken();
    }
}
