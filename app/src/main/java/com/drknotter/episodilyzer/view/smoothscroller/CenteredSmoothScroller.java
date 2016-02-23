package com.drknotter.episodilyzer.view.smoothscroller;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import com.tonicartos.superslim.LayoutManager;

public class CenteredSmoothScroller extends LinearSmoothScroller {
    int[] firstPositions = new int[0];
    int[] lastPositions = new int[0];
    public CenteredSmoothScroller(Context context) {
        super(context);
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getLayoutManager() != null) {
            if (getLayoutManager() instanceof LayoutManager) {
                LayoutManager manager = (LayoutManager) getLayoutManager();
                int firstPosition = manager.findFirstVisibleItemPosition();
                int lastPosition = manager.findLastVisibleItemPosition();
                View v = manager.findViewByPosition(targetPosition);
                return computeScrollVectorRelative(targetPosition, firstPosition, lastPosition, v);

            } else if (getLayoutManager() instanceof StaggeredGridLayoutManager) {
                StaggeredGridLayoutManager manager = (StaggeredGridLayoutManager) getLayoutManager();
                int spanCount = manager.getSpanCount();
                if (firstPositions.length != spanCount) {
                    firstPositions = new int[spanCount];
                }
                if (lastPositions.length != spanCount) {
                    lastPositions = new int[spanCount];
                }
                manager.findFirstVisibleItemPositions(firstPositions);
                manager.findLastVisibleItemPositions(lastPositions);
                View v = manager.findViewByPosition(targetPosition);
                return computeScrollVectorRelative(targetPosition, min(firstPositions), max(lastPositions), v);
            }
        }
        return new PointF(0f, 0f);
    }

    private int min(int[] values) {
        int min = Integer.MAX_VALUE;
        for (int value : values) {
            if (value < min) {
                min = value;
            }
        }
        return min;
    }

    private int max(int[] values) {
        int max = Integer.MIN_VALUE;
        for (int value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    @Override
    public int calculateDyToMakeVisible(View view, int snapPreference) {
        View parent = (View) view.getParent();

        if (parent != null) {
            int childHeight = view.getHeight();
            int parentHeight = parent.getHeight();

            return (parentHeight / 2 - childHeight / 2) - (int) view.getY();
        } else {
            return 0;
        }
    }

    private PointF computeScrollVectorRelative(int targetPosition, int firstPosition, int lastPosition, View v) {
        if (targetPosition < firstPosition) {
            return new PointF(0f, -1f);
        } else if (targetPosition > lastPosition) {
            return new PointF(0f, 1f);
        } else if (v != null) {
            float centerY = v.getY() + 0.5f * v.getHeight();
            float parentCenterY = 0.5f * ((View) v.getParent()).getHeight();
            if (centerY < parentCenterY) {
                return new PointF(0f, -1f);
            } else if (centerY > parentCenterY) {
                return new PointF(0f, 1f);
            }
            return new PointF(0f, 0f);
        }
        return new PointF(0f, 0f);
    }
}
