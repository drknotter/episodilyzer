package com.drknotter.episodilyzer.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import androidx.annotation.NonNull;

import java.io.IOException;

public class SoundUtils {
    public static void rollDice(@NonNull MediaPlayer player, @NonNull Context context) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd("dice_roll.mp3");
            player.reset();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setLooping(false);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void fiddleDice(@NonNull MediaPlayer player, @NonNull Context context) {
        try {
            AssetFileDescriptor afd = context.getAssets().openFd("dice_fiddle.mp3");
            player.reset();
            player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
            player.setLooping(true);
            player.prepare();
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
