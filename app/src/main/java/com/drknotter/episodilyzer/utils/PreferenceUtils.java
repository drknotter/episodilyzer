package com.drknotter.episodilyzer.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.drknotter.episodilyzer.Episodilyzer;

public class PreferenceUtils {
    private static final String UI_PREFS = "ui_prefs";
    private static final String DO_NOT_SHOW_DELETE_AGAIN = "DO_NOT_SHOW_DELETE_AGAIN";

    @SuppressLint("CommitPrefEdits")
    public static void setDoNotShowDeleteAgain(boolean doNotShowDeleteAgain) {
        SharedPreferences prefs = Episodilyzer.getInstance().getSharedPreferences(UI_PREFS, Context.MODE_PRIVATE);
        prefs.edit().putBoolean(DO_NOT_SHOW_DELETE_AGAIN, doNotShowDeleteAgain).commit();
    }

    public static boolean getDoNotShowDeleteAgain() {
        return Episodilyzer.getInstance().getSharedPreferences(UI_PREFS, Context.MODE_PRIVATE)
                .getBoolean(DO_NOT_SHOW_DELETE_AGAIN, false);
    }
}
