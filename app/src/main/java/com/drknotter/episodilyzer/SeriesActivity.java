package com.drknotter.episodilyzer;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.adapter.SeriesAdapter;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.model.Season;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.view.holder.SeasonHeaderViewHolder;
import com.drknotter.episodilyzer.view.holder.SeriesOverviewViewHolder;
import com.drknotter.episodilyzer.view.smoothscroller.CenteredSmoothScroller;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tonicartos.superslim.LayoutManager;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.Bind;
import butterknife.OnClick;

public class SeriesActivity extends RecyclerViewActivity {
    public static final String EXTRA_SERIES_ID = SeriesActivity.class.getCanonicalName() + ".EXTRA_SERIES_ID";

    @Bind(R.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Bind(R.id.toolbar_background)
    ImageView toolbarBackground;

    private List<Object> seriesInfo = new ArrayList<>();
    private Series series;
    private Queue<Episode> randomEpisodeOrder = new ArrayDeque<>();

    private Integer randomEpisodePosition = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SeriesAdapter adapter = new SeriesAdapter(seriesInfo);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator() {
            @Override
            public boolean animateChange(RecyclerView.ViewHolder oldHolder, RecyclerView.ViewHolder newHolder, int fromX, int fromY, int toX, int toY) {
                if (oldHolder instanceof SeasonHeaderViewHolder && newHolder instanceof SeasonHeaderViewHolder) {
                    dispatchChangeFinished(oldHolder, true);
                    dispatchChangeFinished(newHolder, false);
                    return false;
                }
                return super.animateChange(oldHolder, newHolder, fromX, fromY, toX, toY);
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (randomEpisodePosition != null && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    possiblySelectPosition(randomEpisodePosition);
                    randomEpisodePosition = null;
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    unselectAllPositions();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(0);
                if (viewHolder != null && viewHolder instanceof SeriesOverviewViewHolder) {
                    ViewCompat.setElevation(appBarLayout, 0);
                    ((SeriesOverviewViewHolder) viewHolder).adjustFadeOut();
                } else {
                    ViewCompat.setElevation(appBarLayout,
                            getResources().getDimensionPixelSize(R.dimen.appbarlayout_elevation));
                }
            }
        });
        recyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                randomEpisodePosition = null;
                return false;
            }
        });

        handleIntent(getIntent());
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_series;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        int seriesId = intent.getIntExtra(EXTRA_SERIES_ID, -1);
        if (seriesId >= 0) {
            series = new Select().from(Series.class)
                    .where("series_id = ?", seriesId)
                    .executeSingle();
            series.lastAccessed = System.currentTimeMillis();
            series.save();

            //noinspection ConstantConditions
            getSupportActionBar().setTitle(series.seriesName);
            collapsingToolbar.setTitle(series.seriesName);

            Banner bestFanart = series.bestFanart();
            if (bestFanart != null) {
                Picasso.with(this)
                        .load(bestFanart.uri())
                        .into(toolbarBackground, new Callback() {
                            @Override
                            public void onSuccess() {
                                toolbar.setBackgroundColor(Color.TRANSPARENT);
                            }

                            @Override
                            public void onError() {
                                collapsingToolbar.setTitleEnabled(false);
                            }
                        });
            }
            collapsingToolbar.setTitleEnabled(bestFanart != null);

            seriesInfo.clear();
            if (!TextUtils.isEmpty(series.overview)) {
                seriesInfo.add(series.seriesOverview());
            }

            int seasonNumber = Integer.MIN_VALUE;
            //noinspection unchecked
            for(List<Episode> episodeList : new List[] {series.episodes(), series.specialEpisodes()}) {
                for (Episode episode : episodeList) {
                    if (episode.seasonNumber != seasonNumber) {
                        seasonNumber = episode.seasonNumber;
                        seriesInfo.add(new Season(episode.seasonId, episode.seasonNumber));
                    }
                    seriesInfo.add(episode);
                }
            }
            recyclerView.getAdapter().notifyDataSetChanged();
        } else {
            finish();
        }
    }

    @OnClick(R.id.fab)
    public void randomEpisode() {
        if (randomEpisodeOrder.size() == 0) {
            newRandomOrder();
        }

        Episode random;
        //noinspection StatementWithEmptyBody
        while ((random = randomEpisodeOrder.poll()) != null && !random.selected);

        if (random != null) {
            appBarLayout.setExpanded(false, true);
            unselectAllPositions();

            for (int i = 0; i < seriesInfo.size(); i++) {
                Object model = seriesInfo.get(i);
                if (model instanceof Episode
                        && ((Episode) model).id == random.id) {
                    randomEpisodePosition = i;
                    possiblySelectPosition(i);
                    recyclerView.smoothScrollToPosition(i);
                    break;
                }
            }
        } else {
            newRandomOrder();
        }
    }

    private void newRandomOrder() {
        randomEpisodeOrder.clear();
        //noinspection unchecked
        randomEpisodeOrder.addAll((List) new Select().from(Episode.class)
                .where("series = ?", series.getId())
                .orderBy("RANDOM()")
                .execute());
    }

    private void possiblySelectPosition(int position) {
        RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
        if (viewHolder != null) {
            viewHolder.itemView.setSelected(true);
        }
    }

    private void unselectAllPositions() {
        for (int i=0; i<seriesInfo.size(); i++ ) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.itemView.setSelected(false);
            }
        }
    }

    @Override
    protected int getMaxColumnWidthPixels() {
        return getResources().getDisplayMetrics().widthPixels;
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LayoutManager(this) {
            private CenteredSmoothScroller smoothScroller = new CenteredSmoothScroller(SeriesActivity.this);
            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }
        };
    }

}
