package com.drknotter.episodilyzer;

import android.app.Application;

import java.io.File;

public class Episodilyzer extends Application {
    private static Episodilyzer sInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
    }

    public static Episodilyzer getInstance() {
        return sInstance;
    }

    public File getRootSeriesDir() {
        File rootDir = new File(getExternalFilesDir(null),
                "series");
        rootDir.mkdirs();
        return rootDir;
    }

    public static class Constants {
        public static final float BANNER_ASPECT_RATIO = 758f / 140f;
    }
}
