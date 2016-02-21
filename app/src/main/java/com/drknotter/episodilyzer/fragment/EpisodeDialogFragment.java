package com.drknotter.episodilyzer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.view.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EpisodeDialogFragment extends AppCompatDialogFragment {
    public static final String ARG_EPISODE_ID = "ARG_EPISODE_ID";

    @Bind(R.id.episode_image)
    AspectRatioImageView episodeImage;
    @Bind(R.id.episode_name)
    TextView episodeName;
    @Bind(R.id.directors)
    TextView directors;
    @Bind(R.id.writers)
    TextView writers;
    @Bind(R.id.guest_starring)
    TextView guestStarring;
    @Bind(R.id.overview)
    TextView overview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.dialog_episode, container, false);
        ButterKnife.bind(this, root);

        int episodeId = 0;
        if (getArguments() != null) {
            episodeId = getArguments().getInt(ARG_EPISODE_ID);
        }

        if (episodeId != 0) {
            Episode episode = new Select()
                    .from(Episode.class)
                    .where("episode_id = ?", episodeId)
                    .executeSingle();
            bindEpisode(episode);
        } else {
            dismissAllowingStateLoss();
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void bindEpisode(Episode episode) {
        // Bind the episode image, if present.
        Uri episodeImageUri = episode.imageUri();
        episodeImage.setVisibility(episodeImageUri != null ? View.VISIBLE : View.GONE);
        Picasso.with(getContext())
                .load(episodeImageUri)
                .into(episodeImage);
        if (episode.thumbWidth > 0 && episode.thumbHeight > 0) {
            episodeImage.setAspectRatio((float) episode.thumbWidth / episode.thumbHeight);
        }

        // Bind the episode name.
        episodeName.setText(episode.episodeName);

        // Bind the episode directors, if present.
        List<String> directors = episode.directors();
        this.directors.setVisibility(directors.size() > 0 ? View.VISIBLE : View.GONE);
        this.directors.setText(String.format(getResources().getQuantityString(R.plurals.director, directors.size()),
                TextUtils.join(", ", directors)));
    }
}
