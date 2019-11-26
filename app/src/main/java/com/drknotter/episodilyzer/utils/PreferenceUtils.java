package com.drknotter.episodilyzer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.drknotter.episodilyzer.Episodilyzer;

public class PreferenceUtils {
    private static final String UI_PREFS = "ui_prefs";
    private static final String DO_NOT_SHOW_DELETE_AGAIN = "DO_NOT_SHOW_DELETE_AGAIN";

    private static final String AUTH_PREFS = "auth_prefs";
    private static final String AUTH_TOKEN = "token";

    @SuppressLint("CommitPrefEdits")
    public static void setDoNotShowDeleteAgain(boolean doNotShowDeleteAgain) {
        Episodilyzer.getInstance()
                .getSharedPreferences(UI_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putBoolean(DO_NOT_SHOW_DELETE_AGAIN, doNotShowDeleteAgain)
                .apply();
    }

    public static boolean getDoNotShowDeleteAgain() {
        return Episodilyzer.getInstance()
                .getSharedPreferences(UI_PREFS, Context.MODE_PRIVATE)
                .getBoolean(DO_NOT_SHOW_DELETE_AGAIN, false);
    }

    public static void setAuthToken(String authToken) {
        Episodilyzer.getInstance()
                .getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)
                .edit()
                .putString(AUTH_TOKEN, authToken)
                .apply();
    }

    public static String getAuthToken() {
        return Episodilyzer.getInstance()
                .getSharedPreferences(AUTH_PREFS, Context.MODE_PRIVATE)
                .getString(AUTH_TOKEN, null);
    }
}
