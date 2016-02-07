package com.drknotter.episodilyzer;

import android.content.Intent;
import android.os.Bundle;

import com.drknotter.episodilyzer.adapter.SeriesAdapter;

import java.util.ArrayList;
import java.util.List;

public class SeriesActivity extends RecyclerViewActivity {
    public static final String EXTRA_SERIES_ID = SeriesActivity.class.getCanonicalName() + ".EXTRA_SERIES_ID";

    List<Object> seriesInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        recyclerView.setAdapter(new SeriesAdapter(seriesInfo));

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

    }
}
