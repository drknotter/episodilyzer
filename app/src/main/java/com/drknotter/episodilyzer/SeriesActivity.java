package com.drknotter.episodilyzer;

import android.content.Intent;
import android.os.Bundle;

public class SeriesActivity extends RecyclerViewActivity {
    public static final String EXTRA_SERIES_ID = SeriesActivity.class.getCanonicalName() + ".EXTRA_SERIES_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

    }
}
