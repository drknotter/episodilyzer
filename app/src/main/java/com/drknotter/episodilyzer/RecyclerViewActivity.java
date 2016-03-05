package com.drknotter.episodilyzer;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.drknotter.episodilyzer.view.decoration.StaggeredGridItemDecoration;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RecyclerViewActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.empty_image)
    ImageView emptyImage;
    @Bind(R.id.empty_text)
    TextView emptyText;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        recyclerView.setLayoutManager(getLayoutManager());
        RecyclerView.ItemDecoration decoration = getItemDecoration();
        if (decoration != null) {
            recyclerView.addItemDecoration(decoration);
        }
    }

    @LayoutRes
    protected int getContentViewId() {
        return R.layout.activity_recycler_view;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    protected int getSpanCount() {
        return getResources().getDisplayMetrics().widthPixels / getMaxColumnWidthPixels();
    }

    protected int getMaxColumnWidthPixels() {
        return getResources().getDimensionPixelSize(R.dimen.max_column_width);
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new StaggeredGridItemDecoration(getResources().getDimensionPixelSize(R.dimen.series_view_margin));
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL) {
            // The default implementation of StaggeredGridLayoutManager does not handle item decorations
            // correctly during animations when this returns true, so we force it to return false.
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
    }
}
