package com.drknotter.episodilyzer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.drknotter.episodilyzer.R;

/**
 * Created by plunkett on 1/23/16.
 */
public class AspectRatioImageView extends ImageView {
    private float aspectRatio = 0f;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView);
            aspectRatio = array.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 0f);
            array.recycle();
        }
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (aspectRatio <= 0f) {
            return;
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        float viewAspectRatio = (float) width / height;

        if (viewAspectRatio > aspectRatio) {
            super.onMeasure(widthMeasureSpec,
                    MeasureSpec.makeMeasureSpec((int) (width / aspectRatio), MeasureSpec.EXACTLY));
        } else {
            super.onMeasure(MeasureSpec.makeMeasureSpec((int) (height * aspectRatio), MeasureSpec.EXACTLY),
                    heightMeasureSpec);
        }
    }
}
