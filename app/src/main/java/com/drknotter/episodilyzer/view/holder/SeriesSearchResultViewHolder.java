package com.drknotter.episodilyzer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeriesSearchResultViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.download_button)
    View downloadButton;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;

    @Bind(R.id.info_button)
    View infoButton;

    public SeriesSearchResultViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindSearchResult(final SaveSeriesInfo saveSeriesInfo) {
        title.setText(saveSeriesInfo.seriesName);
        overview.setText(saveSeriesInfo.overview);
        overview.setVisibility(saveSeriesInfo.overview != null ? View.VISIBLE : View.GONE);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesUtils.saveSeries(saveSeriesInfo);
            }
        });
    }
}
