package com.drknotter.episodilyzer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.drknotter.episodilyzer.view.decoration.SeriesViewDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeriesListActivity extends AppCompatActivity {
    @Bind(R.id.series_list)
    RecyclerView seriesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
        seriesList.setLayoutManager(manager);
        seriesList.addItemDecoration(new SeriesViewDecoration(getResources().getDimensionPixelSize(R.dimen.series_view_margin)));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) seriesList.getLayoutManager();
        int newSpanCount = getSpanCount();
        if (manager.getSpanCount() != newSpanCount) {
            manager.setSpanCount(newSpanCount);
            seriesList.invalidateItemDecorations();
        }
    }

    private int getSpanCount() {
        return getResources().getDisplayMetrics().widthPixels / getResources().getDimensionPixelSize(R.dimen.max_column_width);
    }
}
