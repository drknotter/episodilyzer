package com.drknotter.episodilyzer.view.decoration;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import hugo.weaving.DebugLog;

public class SeriesViewDecoration extends RecyclerView.ItemDecoration {
    private int margin = 0;
    private Boolean lastIsTop = null;
    private Boolean lastIsLeft = null;

    public SeriesViewDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }

    @DebugLog
    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        boolean isTop = false;
        boolean isLeft = false;
        int position = parent.getChildAdapterPosition(view);

        if (position != RecyclerView.NO_POSITION) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            isTop = position < ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount();
            isLeft = params.getSpanIndex() == 0;
        } else if (lastIsTop != null && lastIsLeft != null) {
            isTop = lastIsTop;
            isLeft = lastIsLeft;
        }

        lastIsTop = isTop;
        lastIsLeft = isLeft;
        outRect.set(isLeft ? margin : 0, isTop ? margin : 0, margin, margin);
    }
}
