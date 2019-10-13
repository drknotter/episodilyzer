package com.drknotter.episodilyzer.view.holder;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.SeriesOverview;

public class SeriesOverviewViewHolder extends BindableViewHolder<SeriesOverview> {
    TextView firstAired;
    TextView overview;
    TextView starring;
    View overviewFadeOut;

    public SeriesOverviewViewHolder(View itemView) {
        super(itemView);

        firstAired = itemView.findViewById(R.id.first_aired);
        overview = itemView.findViewById(R.id.overview);
        starring = itemView.findViewById(R.id.starring);
        overviewFadeOut = itemView.findViewById(R.id.overview_fade_out);
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
