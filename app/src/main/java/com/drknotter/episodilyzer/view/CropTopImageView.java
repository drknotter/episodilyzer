package com.drknotter.episodilyzer.view;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

public class CropTopImageView extends ImageView {
    public CropTopImageView(Context context) {
        super(context);
        initialize();
    }

    public CropTopImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CropTopImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        setScaleType(ScaleType.MATRIX);
    }

    @Override
    protected boolean setFrame(int frameLeft, int frameTop, int frameRight, int frameBottom) {
        if (getDrawable() == null) {
            return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
        }

        float frameWidth = frameRight - frameLeft;
        float frameHeight = frameBottom - frameTop;

        float originalImageWidth = (float) getDrawable().getIntrinsicWidth();
        float originalImageHeight = (float) getDrawable().getIntrinsicHeight();

        float fitHorizontallyScaleFactor = frameWidth / originalImageWidth;
        float fitVerticallyScaleFactor = frameHeight / originalImageHeight;
        float usedScaleFactor = Math.max(fitHorizontallyScaleFactor, fitVerticallyScaleFactor);

        Matrix matrix = getImageMatrix();
        matrix.setScale(usedScaleFactor, usedScaleFactor, 0, 0);
        setImageMatrix(matrix);
        return super.setFrame(frameLeft, frameTop, frameRight, frameBottom);
    }
}
