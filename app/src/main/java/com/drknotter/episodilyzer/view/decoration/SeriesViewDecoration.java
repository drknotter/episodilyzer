package com.drknotter.episodilyzer.view.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class SeriesViewDecoration extends RecyclerView.ItemDecoration {
    private int margin = 0;

    public SeriesViewDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        boolean isTop = false;
        boolean isLeft = false;
        int position = parent.getChildAdapterPosition(view);
        if (position != RecyclerView.NO_POSITION) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            isTop = position < ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount();
            isLeft = params.getSpanIndex() == 0;
        }

        outRect.set(isLeft ? margin : 0, isTop ? margin : 0, margin, margin);
    }
}
