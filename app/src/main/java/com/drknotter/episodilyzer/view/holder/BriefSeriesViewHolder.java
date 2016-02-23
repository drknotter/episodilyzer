package com.drknotter.episodilyzer.view.holder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.SeriesActivity;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.BriefSeries;
import com.drknotter.episodilyzer.server.task.SaveSeriesAsyncTask;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BriefSeriesViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.banner)
    ImageView banner;

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.firstAired)
    TextView firstAired;
    @Bind(R.id.star_toggle)
    View starToggle;

    @Bind(R.id.overview)
    TextView overview;

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
        banner.setVisibility(bannerUri != null ? View.VISIBLE : View.GONE);
        title.setText(briefSeries.seriesName);
        setFirstAired(briefSeries.firstAired);
        setOverview(briefSeries.overview);

        starToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                starToggle.setActivated(!starToggle.isActivated());

                if (starToggle.isActivated()) {
                    new SaveSeriesAsyncTask().execute(briefSeries.seriesId);
                } else {
                    SeriesUtils.deleteSeries(briefSeries.seriesId);
                }
            }
        });
        starToggle.setActivated(SeriesUtils.isSeriesSaved(briefSeries.seriesId));
    }

    public void bindSeries(final Series series, final View.OnClickListener starClickListener) {
        if (series != null) {
            Banner bestBanner = series.bestBanner();

            Uri bannerUri = null;
            if (bestBanner != null) {
                bannerUri = Uri.parse(TheTVDBService.BASE_URL)
                        .buildUpon()
                        .appendPath("banners")
                        .appendPath(bestBanner.path)
                        .build();
            }
            Picasso.with(itemView.getContext())
                    .load(bannerUri)
                    .into(banner);
            banner.setVisibility(bannerUri != null ? View.VISIBLE : View.GONE);
            title.setText(series.seriesName);
            setFirstAired(series.firstAired);
            setOverview(series.overview);

            starToggle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    starToggle.setActivated(!starToggle.isActivated());
                    SeriesUtils.deleteSeries(series.id);
                    starClickListener.onClick(v);
                }
            });
            starToggle.setActivated(SeriesUtils.isSeriesSaved(series.id));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent seriesIntent = new Intent(itemView.getContext(), SeriesActivity.class);
                    seriesIntent.putExtra(SeriesActivity.EXTRA_SERIES_ID, series.id);
                    itemView.getContext().startActivity(seriesIntent);
                }
            });
        }
    }

    private void setFirstAired(String firstAiredText) {
        try {
            Date firstAiredDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAiredText);
            firstAired.setText(String.format(itemView.getResources().getString(R.string.first_aired),
                    new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(firstAiredDate)));
            firstAired.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            firstAired.setText(null);
            firstAired.setVisibility(View.GONE);
        }
    }

    private void setOverview(String overviewText) {
        overview.setText(overviewText);
        overview.setVisibility(overviewText != null ? View.VISIBLE : View.GONE);
    }
}
