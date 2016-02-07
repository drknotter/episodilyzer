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

public class RecyclerViewActivity extends AppCompatActivity {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new SeriesViewDecoration(getResources().getDimensionPixelSize(R.dimen.series_view_margin)));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    recyclerView.invalidateItemDecorations();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) recyclerView.getLayoutManager();
        int newSpanCount = getSpanCount();
        if (manager.getSpanCount() != newSpanCount) {
            manager.setSpanCount(newSpanCount);
            recyclerView.invalidateItemDecorations();
        }
    }

    private int getSpanCount() {
        return getResources().getDisplayMetrics().widthPixels / getMaxColumnWidthPixels();
    }

    protected int getMaxColumnWidthPixels() {
        return getResources().getDimensionPixelSize(R.dimen.max_column_width);
    }
}
