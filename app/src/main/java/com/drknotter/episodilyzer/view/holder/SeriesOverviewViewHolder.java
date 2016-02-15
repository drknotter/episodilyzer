package com.drknotter.episodilyzer.view.holder;

import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.SeriesOverview;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeriesOverviewViewHolder extends BindableViewHolder<SeriesOverview> {
    @Bind(R.id.overview)
    TextView overview;
    public SeriesOverviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(SeriesOverview model) {
        overview.setText(model.overview);
    }
}
