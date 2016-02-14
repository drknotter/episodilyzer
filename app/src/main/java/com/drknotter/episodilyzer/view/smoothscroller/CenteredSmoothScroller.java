package com.drknotter.episodilyzer.view.smoothscroller;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.LinearSmoothScroller;
import android.view.View;

import com.tonicartos.superslim.LayoutManager;

public class CenteredSmoothScroller extends LinearSmoothScroller {
    public CenteredSmoothScroller(Context context) {
        super(context);
    }

    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        if (getLayoutManager() != null && getLayoutManager() instanceof LayoutManager) {
            LayoutManager manager = (LayoutManager) getLayoutManager();
            int firstPosition = manager.findFirstVisibleItemPosition();
            int lastPosition = manager.findLastVisibleItemPosition();

            if (targetPosition < firstPosition) {
                return new PointF(0f, -1f);
            } else if (targetPosition > lastPosition) {
                return new PointF(0f, 1f);
            } else {
                View v = manager.findViewByPosition(targetPosition);
                if (v != null) {
                    float centerY = v.getY() + 0.5f * v.getHeight();
                    float parentCenterY = 0.5f * ((View) v.getParent()).getHeight();
                    if (centerY < parentCenterY) {
                        return new PointF(0f, -1f);
                    } else if (centerY > parentCenterY) {
                        return new PointF(0f, 1f);
                    }
                    return new PointF(0f, 0f);
                }
            }
        }
        return new PointF(0f, 0f);
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
}
