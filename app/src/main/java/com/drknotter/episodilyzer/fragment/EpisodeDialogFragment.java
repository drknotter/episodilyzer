package com.drknotter.episodilyzer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.utils.PicassoUtils;
import com.drknotter.episodilyzer.view.AspectRatioImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class EpisodeDialogFragment extends AppCompatDialogFragment {
    public static final String TAG = EpisodeDialogFragment.class.getSimpleName();
    public static final String ARG_EPISODE_ID = "ARG_SERIES_BANNER";

    @BindView(R.id.episode_image)
    AspectRatioImageView episodeImage;
    @BindView(R.id.episode_name)
    TextView episodeName;
    @BindView(R.id.directors)
    TextView directors;
    @BindView(R.id.writers)
    TextView writers;
    @BindView(R.id.guest_starring)
    TextView guestStarring;
    @BindView(R.id.overview)
    TextView overview;

    private Unbinder unbinder;

    public static EpisodeDialogFragment newInstance(int episodeId) {
        EpisodeDialogFragment fragment = new EpisodeDialogFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_EPISODE_ID, episodeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View root = inflater.inflate(R.layout.dialog_episode, container, false);
        unbinder = ButterKnife.bind(this, root);

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
        unbinder.unbind();
    }

    private void bindEpisode(Episode episode) {
        // Bind the episode image, if present.
        Uri episodeImageUri = episode.imageUri();
        episodeImage.setVisibility(episodeImageUri != null ? View.VISIBLE : View.GONE);
        if (episode.episodeImageFlag == 1) {
            episodeImage.setAspectRatio(4f / 3f);
        } else {
            episodeImage.setAspectRatio(16f / 9f);
        }
        PicassoUtils.getPicasso(getContext())
                .load(episodeImageUri)
                .into(episodeImage);

        // Bind the episode name.
        episodeName.setText(episode.getName());

        // Bind the episode directors, if present.
        List<String> directors = episode.directors();
        this.directors.setVisibility(directors.size() > 0 ? View.VISIBLE : View.GONE);
        this.directors.setText(getResources().getQuantityString(R.plurals.director, directors.size(),
                TextUtils.join(", ", directors)));

        // Bind the episode writers, if present.
        List<String> writers = episode.writers();
        this.writers.setVisibility(writers.size() > 0 ? View.VISIBLE : View.GONE);
        this.writers.setText(getResources().getQuantityString(R.plurals.writer, writers.size(),
                TextUtils.join(", ", writers)));

        // Bind the guest stars, if present.
        List<String> guestStars = episode.guestStars();
        this.guestStarring.setVisibility(guestStars.size() > 0 ? View.VISIBLE : View.GONE);
        this.guestStarring.setText(getResources().getQuantityString(R.plurals.guest_star, guestStars.size(),
                TextUtils.join(", ", guestStars)));

        // Bind the overview, if present.
        this.overview.setVisibility(TextUtils.isEmpty(episode.overview) ? View.GONE : View.VISIBLE);
        this.overview.setText(episode.overview);
    }
}
