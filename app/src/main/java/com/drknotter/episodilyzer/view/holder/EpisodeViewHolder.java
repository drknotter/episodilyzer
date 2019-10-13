package com.drknotter.episodilyzer.view.holder;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.fragment.EpisodeDialogFragment;
import com.drknotter.episodilyzer.model.Episode;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EpisodeViewHolder extends BindableViewHolder<Episode> {
    public interface OnEpisodeSelectedChangeListener {
        void onEpisodeSelectedChange(Episode e, boolean selected);
    }

    TextView title;
    TextView firstAired;
    TextView overview;
    CheckBox selected;

    private OnEpisodeSelectedChangeListener listener;

    public EpisodeViewHolder(View itemView, OnEpisodeSelectedChangeListener listener) {
        super(itemView);

        title = itemView.findViewById(R.id.title);
        firstAired = itemView.findViewById(R.id.first_aired);
        overview = itemView.findViewById(R.id.overview);
        selected = itemView.findViewById(R.id.selected);

        this.listener = listener;
    }

    @Override
    public void bind(final Episode episode) {
        title.setText(String.format(itemView.getResources().getString(R.string.episode_number_title),
                        episode.episodeNumber, episode.getName()));
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
                    FragmentManager fragmentManager = ((AppCompatActivity) v.getContext()).getSupportFragmentManager();
                    EpisodeDialogFragment fragment = (EpisodeDialogFragment) fragmentManager.findFragmentByTag(EpisodeDialogFragment.TAG);
                    if (fragment != null) {
                        fragment.dismissAllowingStateLoss();
                    }
                    fragment = EpisodeDialogFragment.newInstance(episode.id);
                    fragment.show(fragmentManager, EpisodeDialogFragment.TAG);
                }
            }
        });
    }

    private void setFirstAired(String firstAiredText) {
        try {
            Date firstAiredDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAiredText);
            if (firstAiredDate != null) {
                firstAired.setText(
                        new SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(firstAiredDate));
            }
            firstAired.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            firstAired.setText(null);
            firstAired.setVisibility(View.GONE);
        }
    }

}
