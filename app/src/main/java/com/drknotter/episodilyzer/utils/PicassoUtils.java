package com.drknotter.episodilyzer.utils;

import android.content.Context;

import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import okhttp3.OkHttpClient;

/**
 * Created by plunkett on 1/20/18.
 */

public class PicassoUtils {
    public static Picasso getPicasso(Context context) {
        return new Picasso.Builder(context)
                .downloader(new OkHttp3Downloader(new OkHttpClient()))
                .build();
    }
}
