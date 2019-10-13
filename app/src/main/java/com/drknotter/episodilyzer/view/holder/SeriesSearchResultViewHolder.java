package com.drknotter.episodilyzer.view.holder;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.fragment.SeriesDialogFragment;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SeriesSearchResultViewHolder extends RecyclerView.ViewHolder {
    View downloadButton;
    TextView title;
    TextView firstAired;
    TextView overview;

    public SeriesSearchResultViewHolder(View itemView) {
        super(itemView);
        downloadButton = itemView.findViewById(R.id.download_button);
        title = itemView.findViewById(R.id.title);
        firstAired = itemView.findViewById(R.id.first_aired);
        overview = itemView.findViewById(R.id.overview);
    }

    public void bindSearchResult(final SaveSeriesInfo saveSeriesInfo) {
        title.setText(saveSeriesInfo.seriesName);
        setFirstAired(saveSeriesInfo.firstAired);
        overview.setText(saveSeriesInfo.overview);
        overview.setVisibility(saveSeriesInfo.overview != null ? View.VISIBLE : View.GONE);

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeriesUtils.saveSeries(saveSeriesInfo);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof Activity) {
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    SeriesDialogFragment fragment = (SeriesDialogFragment) fragmentManager.findFragmentByTag(SeriesDialogFragment.TAG);
                    if (fragment != null) {
                        fragment.dismissAllowingStateLoss();
                    }
                    fragment = SeriesDialogFragment.newInstance(saveSeriesInfo);
                    fragment.show(fragmentManager, SeriesDialogFragment.TAG);
                }
            }
        });
    }

    private void setFirstAired(String firstAiredText) {
        try {
            Date firstAiredDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAiredText);
            firstAired.setText(new SimpleDateFormat("MM/d/yyyy", Locale.getDefault()).format(firstAiredDate));
            firstAired.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            firstAired.setText(null);
            firstAired.setVisibility(View.GONE);
        }
    }
}
