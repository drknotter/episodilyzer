package com.drknotter.episodilyzer.view.holder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.BriefSeries;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BriefSeriesViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.banner)
    ImageView banner;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.star_toggle)
    View starToggle;

    public BriefSeriesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindSeries(final BriefSeries briefSeries) {
        Uri bannerUri = Uri.parse(TheTVDBService.BASE_URL)
                .buildUpon()
                .appendPath("banners")
                .appendPath(briefSeries.banner)
                .build();
        Picasso.with(itemView.getContext())
                .load(bannerUri)
                .into(banner);
        title.setText(briefSeries.seriesName);
        overview.setText(briefSeries.overview);

        starToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starToggle.setActivated(!starToggle.isActivated());

                if (starToggle.isActivated()) {
                    SeriesUtils.saveSeries(briefSeries.seriesId);
                } else {
                    SeriesUtils.deleteSeries(briefSeries.seriesId);
                }
            }
        });
        starToggle.setActivated(SeriesUtils.isSeriesSaved(briefSeries.seriesId));
    }
}
