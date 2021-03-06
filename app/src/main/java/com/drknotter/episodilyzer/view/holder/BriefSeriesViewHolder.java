package com.drknotter.episodilyzer.view.holder;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.SeriesActivity;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.utils.PicassoUtils;
import com.drknotter.episodilyzer.utils.PreferenceUtils;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BriefSeriesViewHolder extends RecyclerView.ViewHolder {
    ImageView banner;
    TextView title;
    TextView firstAired;
    View deleteButton;
    TextView overview;

    public BriefSeriesViewHolder(View itemView) {
        super(itemView);
        banner = itemView.findViewById(R.id.banner);
        title = itemView.findViewById(R.id.title);
        firstAired = itemView.findViewById(R.id.first_aired);
        deleteButton = itemView.findViewById(R.id.delete_button);
        overview = itemView.findViewById(R.id.overview);
    }

    public void bindSeries(final Series series, final View.OnClickListener deleteClickListener) {
        if (series != null) {
            Banner bestBanner = series.bestBanner();

            Uri bannerUri = null;
            if (bestBanner != null) {
                bannerUri = Uri.parse(TheTVDBService.WEB_URL)
                        .buildUpon()
                        .appendPath("banners")
                        .appendPath(bestBanner.path)
                        .build();
            }
            PicassoUtils.getPicasso(itemView.getContext())
                    .load(bannerUri)
                    .into(banner);
            banner.setVisibility(bannerUri != null ? View.VISIBLE : View.GONE);
            title.setText(series.seriesName);
            setFirstAired(series.firstAired);
            setOverview(series.overview);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (PreferenceUtils.getDoNotShowDeleteAgain()) {
                        doDelete();
                    } else {
                        final View checkboxView = LayoutInflater.from(v.getContext()).inflate(R.layout.view_dialog_checkbox, null);
                        ((TextView) checkboxView.findViewById(R.id.text)).setText(R.string.dont_show_again);
                        new AlertDialog.Builder(v.getContext())
                                .setTitle(R.string.delete_show_title)
                                .setMessage(R.string.delete_show_message)
                                .setPositiveButton(R.string.delete_show_button, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PreferenceUtils.setDoNotShowDeleteAgain(
                                                ((CheckBox) checkboxView.findViewById(R.id.checkbox)).isChecked());
                                        doDelete();
                                    }
                                })
                                .setView(checkboxView)
                                .show();
                    }
                }

                private void doDelete() {
                    SeriesUtils.deleteSeries(series.id);
                    deleteClickListener.onClick(deleteButton);
                }
            });

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
