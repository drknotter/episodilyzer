package com.drknotter.episodilyzer.view.holder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.drknotter.episodilyzer.EpisodeActivity;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Episode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EpisodeViewHolder extends BindableViewHolder<Episode> {
    public interface OnEpisodeSelectedChangeListener {
        void onEpisodeSelectedChange(Episode e, boolean selected);
    }

    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.firstAired)
    TextView firstAired;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.selected)
    CheckBox selected;

    private OnEpisodeSelectedChangeListener listener;

    public EpisodeViewHolder(View itemView, OnEpisodeSelectedChangeListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    @Override
    public void bind(final Episode episode) {
        title.setText(String.format(itemView.getResources().getString(R.string.episode_number_title),
                        episode.episodeNumber, episode.episodeName));
        overview.setText(episode.overview);
        setFirstAired(episode.firstAired);
        selected.setOnCheckedChangeListener(null);
        selected.setChecked(episode.selected);
        selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                episode.selected = isChecked;
                episode.save();
                if (listener != null) {
                    listener.onEpisodeSelectedChange(episode, isChecked);
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getContext() instanceof Activity) {
                    Intent episodeIntent = new Intent(v.getContext(), EpisodeActivity.class);
                    episodeIntent.setData(Uri.parse(Integer.toString(episode.id)));
                    v.getContext().startActivity(episodeIntent);
                }
            }
        });
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
