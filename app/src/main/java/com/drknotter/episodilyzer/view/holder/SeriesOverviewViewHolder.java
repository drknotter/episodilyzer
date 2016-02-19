package com.drknotter.episodilyzer.view.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.SeriesOverview;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeriesOverviewViewHolder extends BindableViewHolder<SeriesOverview> {
    @Bind(R.id.firstAired)
    TextView firstAired;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.starring)
    TextView starring;

    @Bind(R.id.overview_fade_out)
    View overviewFadeOut;

    public SeriesOverviewViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(SeriesOverview model) {
        firstAired.setVisibility(model.firstAired != null ? View.VISIBLE : View.GONE);
        overview.setVisibility(model.overview != null ? View.VISIBLE : View.GONE);
        starring.setVisibility(model.starring != null ? View.VISIBLE : View.GONE);

        firstAired.setText(String.format(itemView.getResources().getString(R.string.first_aired),
                model.firstAired));
        overview.setText(model.overview);
        starring.setText(String.format(itemView.getResources().getString(R.string.starring),
                TextUtils.join(", ", model.starring)));
    }

    public void adjustFadeOut() {
        overviewFadeOut.setTop(-itemView.getTop());
        overviewFadeOut.setBottom(-itemView.getTop() + overviewFadeOut.getMeasuredHeight());
    }
}
