package com.drknotter.episodilyzer;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.view.AspectRatioImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EpisodeActivity extends AppCompatActivity {
    private static final String TAG = EpisodeActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        int episodeId = 0;
        try {
            episodeId = Integer.parseInt(intent.getDataString());
        } catch (Exception e) {
            Log.w(TAG, "Unable to parse intent data " + intent.getDataString());
        }

        if (episodeId != 0) {
            Episode episode = new Select()
                    .from(Episode.class)
                    .where("episode_id = ?", episodeId)
                    .executeSingle();
            bindEpisode(episode);
        } else {
            finish();
        }

    }

    private void bindEpisode(Episode episode) {
        //noinspection ConstantConditions
        getSupportActionBar().setTitle(episode.episodeName);
        collapsingToolbar.setTitle(episode.episodeName);

        // Bind the episode image, if present.
        Uri episodeImageUri = episode.imageUri();
        episodeImage.setVisibility(episodeImageUri != null ? View.VISIBLE : View.GONE);
        collapsingToolbar.setTitleEnabled(episodeImageUri != null);
        Picasso.with(this)
                .load(episodeImageUri)
                .into(episodeImage, new Callback() {
                    @Override
                    public void onSuccess() {
                        toolbar.setBackgroundColor(Color.TRANSPARENT);
                    }

                    @Override
                    public void onError() {
                        collapsingToolbar.setTitleEnabled(false);
                    }
                });
        if (episode.thumbWidth > 0 && episode.thumbHeight > 0) {
            episodeImage.setAspectRatio((float) episode.thumbWidth / episode.thumbHeight);
        }

        // Bind the episode name.
        episodeName.setText(episode.episodeName);

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
