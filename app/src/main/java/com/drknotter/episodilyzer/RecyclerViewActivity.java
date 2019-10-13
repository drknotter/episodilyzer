package com.drknotter.episodilyzer;

import android.os.Build;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.drknotter.episodilyzer.view.decoration.StaggeredGridItemDecoration;

public abstract class RecyclerViewActivity extends AppCompatActivity {
    Toolbar toolbar;

    ImageView emptyImage;
    TextView emptyText;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewId());

        toolbar = findViewById(R.id.toolbar);
        emptyImage = findViewById(R.id.empty_image);
        emptyText = findViewById(R.id.empty_text);
        recyclerView = findViewById(R.id.recycler_view);

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
