package com.drknotter.episodilyzer.view.holder;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by plunkett on 1/26/16.
 */
public class FullSeriesViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.banner)
    ImageView banner;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.star_toggle)
    View starToggle;

    public FullSeriesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bindSeries(final Series series) {
        if (series != null) {
            Banner randomBanner = series.bestBanner();

            Uri bannerUri = null;
            if (randomBanner != null) {
                bannerUri = Uri.parse(TheTVDBService.BASE_URL)
                        .buildUpon()
                        .appendPath("banners")
                        .appendPath(randomBanner.path)
                        .build();
            }
            Picasso.with(itemView.getContext())
                    .load(bannerUri)
                    .into(banner);
            title.setText(series.seriesName);
            overview.setText(series.overview);

            starToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starToggle.setActivated(!starToggle.isActivated());
                    SeriesUtils.deleteSeries(series.id);
                }
            });
            starToggle.setActivated(SeriesUtils.isSeriesSaved(series.id));
        }
    }
}