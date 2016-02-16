package com.drknotter.episodilyzer.view.holder;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.SeriesOverview;

import butterknife.Bind;
import butterknife.ButterKnife;
import hugo.weaving.DebugLog;

public class SeriesOverviewViewHolder extends BindableViewHolder<SeriesOverview> {
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.overview_fade_out)
    View overviewFadeOut;

    public SeriesOverviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @DebugLog
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            }
        });
    }

    @Override
    public void bind(SeriesOverview model) {
        overview.setText(model.overview);
    }

    public void adjustFadeOut() {
        Log.v("FindMe", "itemView.getTop(): " + itemView.getTop());
        overviewFadeOut.setTop(-itemView.getTop());
        overviewFadeOut.setBottom(-itemView.getTop() + overviewFadeOut.getMeasuredHeight());
    }
}
