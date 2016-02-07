package com.drknotter.episodilyzer.view.decoration;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

public class SeriesViewDecoration extends RecyclerView.ItemDecoration {
    private int margin = 0;
    private boolean lastIsTop;
    private boolean lastIsLeft;
    private boolean lastIsRight;

    public SeriesViewDecoration(int margin) {
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        boolean isLeft;
        boolean isTop;
        boolean isRight;

        int position = parent.getChildAdapterPosition(view);
        if (position != RecyclerView.NO_POSITION) {
            StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            int spanCount = ((StaggeredGridLayoutManager) parent.getLayoutManager()).getSpanCount();
            isTop = position < spanCount;
            isLeft = params.getSpanIndex() == 0;
            isRight = params.getSpanIndex() == spanCount - 1;
        } else {
            isTop = lastIsTop;
            isLeft = lastIsLeft;
            isRight = lastIsRight;
        }

        lastIsTop = isTop;
        lastIsLeft = isLeft;
        lastIsRight = isRight;
        outRect.set(isLeft ? margin : margin / 2, isTop ? margin : 0,
                isRight ? margin : margin / 2, margin);
    }

}
