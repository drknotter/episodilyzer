package com.drknotter.episodilyzer.view.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Episode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EpisodeViewHolder extends BindableViewHolder<Episode> {
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.firstAired)
    TextView firstAired;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.selected)
    CheckBox selected;

    public EpisodeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Episode episode) {
        title.setText(episode.episodeName);
        overview.setText(episode.overview);
        setFirstAired(episode.firstAired);
        selected.setChecked(episode.selected);
    }

    private void setFirstAired(String firstAiredText) {
        try {
            Date firstAiredDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAiredText);
            firstAired.setText(
                    new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(firstAiredDate));
            firstAired.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            firstAired.setText(null);
            firstAired.setVisibility(View.GONE);
        }
    }

}
