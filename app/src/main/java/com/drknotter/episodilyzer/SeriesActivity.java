package com.drknotter.episodilyzer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.adapter.SeriesAdapter;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.view.decoration.DividerItemDecoration;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

public class SeriesActivity extends RecyclerViewActivity {
    public static final String EXTRA_SERIES_ID = SeriesActivity.class.getCanonicalName() + ".EXTRA_SERIES_ID";

    List<Episode> episodes = new ArrayList<>();
    Series series;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SeriesAdapter adapter = new SeriesAdapter(episodes);
        recyclerView.setAdapter(adapter);
        final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration((StickyRecyclerHeadersAdapter) recyclerView.getAdapter());
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                headersDecor.invalidateHeaders();
            }
        });
        recyclerView.addItemDecoration(headersDecor);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        int seriesId = intent.getIntExtra(EXTRA_SERIES_ID, -1);
        if (seriesId >= 0) {
            series = new Select().from(Series.class)
                    .where("series_id = ?", seriesId)
                    .executeSingle();
            series.lastAccessed = System.currentTimeMillis();
            series.save();

            //noinspection ConstantConditions
            getSupportActionBar().setTitle(series.seriesName);
            collapsingToolbar.setTitle(series.seriesName);
            AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) collapsingToolbar.getLayoutParams();
            params.setScrollFlags(
                    AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                            | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS_COLLAPSED
                            | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED
                            | AppBarLayout.LayoutParams.SCROLL_FLAG_SNAP);
            collapsingToolbar.setLayoutParams(params);

            Banner bestFanart = series.bestFanart();
            if (bestFanart != null) {
                Picasso.with(this)
                        .load(bestFanart.uri())
                        .into(toolbarBackground, new Callback() {
                            @Override
                            public void onSuccess() {
                                toolbar.setBackgroundColor(Color.TRANSPARENT);
                            }

                            @Override
                            public void onError() {
                            }
                        });
            }
            collapsingToolbar.setTitleEnabled(bestFanart != null);

            episodes.clear();
            episodes.addAll(series.episodes());
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            finish();
        }
    }

    @Override
    protected int getMaxColumnWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getResources().getDrawable(R.drawable.divider));
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }
}
