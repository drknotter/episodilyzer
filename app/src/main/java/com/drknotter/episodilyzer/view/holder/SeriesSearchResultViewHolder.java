package com.drknotter.episodilyzer.view.holder;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.fragment.SeriesDialogFragment;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SeriesSearchResultViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.download_button)
    View downloadButton;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.firstAired)
    TextView firstAired;
    @BindView(R.id.overview)
    TextView overview;

    public SeriesSearchResultViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
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
