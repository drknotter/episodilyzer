package com.drknotter.episodilyzer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.drknotter.episodilyzer.view.decoration.StaggeredGridItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {
    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.toolbar_background)
    ImageView toolbarBackground;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL) {
            // The default implementation of StaggeredGridLayoutManager does not handle item decorations
            // correctly during animations when this returns true, so we force it to return false.
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
        recyclerView.setLayoutManager(manager);
        recyclerView.addItemDecoration(new StaggeredGridItemDecoration(getResources().getDimensionPixelSize(R.dimen.series_view_margin)));
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
