package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.adapter.EpisodilyzerAdapter;
import com.drknotter.episodilyzer.event.SeriesSaveFailEvent;
import com.drknotter.episodilyzer.event.SeriesSaveStartEvent;
import com.drknotter.episodilyzer.event.SeriesSaveSuccessEvent;
import com.drknotter.episodilyzer.fragment.AboutDialogFragment;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.drknotter.episodilyzer.utils.SoundUtils;
import com.drknotter.episodilyzer.view.smoothscroller.CenteredSmoothScroller;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.seismic.ShakeDetector;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class EpisodilyzerActivity extends RecyclerViewActivity implements ShakeDetector.Listener {
    private List<Series> myShows = new ArrayList<>();

    private Queue<Series> randomSeriesOrder = new ArrayDeque<>();
    private Integer randomSeriesPosition = null;

    private ShakeDetector shakeDetector;
    private MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EpisodilyzerAdapter adapter = new EpisodilyzerAdapter(myShows);
        adapter.setHasStableIds(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int lastState = -1;

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                lastState = newState;
                if (randomSeriesPosition != null && newState == RecyclerView.SCROLL_STATE_IDLE) {
                    possiblySelectPosition(randomSeriesPosition);
                    randomSeriesPosition = null;
                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    unselectAllPositions();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayAllSeries();

        if (shakeDetector == null) {
            shakeDetector = new ShakeDetector(this);
        }
        shakeDetector.start((SensorManager) getSystemService(SENSOR_SERVICE));
        if (player != null) {
            player.release();
        }
        player = new MediaPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
        if (player != null) {
            player.release();
            player = null;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episodilyzer, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                FragmentManager fragmentMananager = getSupportFragmentManager();
                AboutDialogFragment fragment = (AboutDialogFragment) fragmentMananager.findFragmentByTag(AboutDialogFragment.TAG);
                if (fragment != null) {
                    fragment.dismissAllowingStateLoss();
                }
                fragment = AboutDialogFragment.newInstance();
                fragment.show(fragmentMananager, AboutDialogFragment.TAG);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_episodilyzer;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new StaggeredGridLayoutManager(getSpanCount(), StaggeredGridLayoutManager.VERTICAL) {
            private CenteredSmoothScroller smoothScroller = new CenteredSmoothScroller(EpisodilyzerActivity.this);

            @Override
            public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
                smoothScroller.setTargetPosition(position);
                startSmoothScroll(smoothScroller);
            }

            // The default implementation of StaggeredGridLayoutManager does not handle item decorations
            // correctly during animations when this returns true, so we force it to return false.
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeriesSaveStartEvent(SeriesSaveStartEvent event) {
        Snackbar.make(recyclerView,
                getString(
                        R.string.snack_saving_series,
                        event.searchResult.seriesName),
                Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeriesSaveSuccessEvent(SeriesSaveSuccessEvent event) {
        Snackbar.make(recyclerView, getString(R.string.snack_series_saved, event.series.seriesName), Snackbar.LENGTH_SHORT).show();
        displayAllSeries();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSeriesSaveFailEvent(SeriesSaveFailEvent event) {
        String message = getString(R.string.snack_series_save_failed, event.searchResult.seriesName);
        if (event.reason == SeriesSaveFailEvent.Reason.NO_RESPONSE) {
            message = getString(R.string.snack_series_save_failed_no_response, event.searchResult.seriesName);
        } else if (event.reason == SeriesSaveFailEvent.Reason.NETWORK) {
                    message = getString(R.string.snack_series_save_failed_network_error, event.searchResult.seriesName);
        } else if (!TextUtils.isEmpty(event.message)) {
             message = getString(R.string.snack_series_save_failed_with_message, event.searchResult.seriesName, event.message);
        }

        Snackbar.make(recyclerView,
                message,
                Snackbar.LENGTH_LONG).show();

        displayAllSeries();
    }

    public void randomSeries() {
        if (randomSeriesOrder.size() == 0) {
            newRandomOrder();
        }

        Series random = randomSeriesOrder.poll();

        if (random != null) {
            unselectAllPositions();

            for (int i = 0; i < myShows.size(); i++) {
                Series model = myShows.get(i);
                if (model.id == random.id) {
                    randomSeriesPosition = i;
                    possiblySelectPosition(i);
                    recyclerView.smoothScrollToPosition(i);
                    break;
                }
            }

            if (player != null) {
                SoundUtils.fiddleDice(player, this);
            }
        } else {
            newRandomOrder();
        }
    }

    private void displayAllSeries() {
        myShows.clear();
        myShows.addAll(SeriesUtils.allSeries());
        recyclerView.getAdapter().notifyDataSetChanged();

        emptyText.setVisibility(myShows.size() == 0 ? View.VISIBLE : View.GONE);
        emptyImage.setVisibility(myShows.size() == 0 ? View.VISIBLE : View.GONE);
        if (myShows.size() == 0) {
            String message = getString(R.string.no_series_saved);
            String replacement = "{ICON}";
            int index = message.indexOf(replacement);

            SpannableString spannableString = new SpannableString(message);
            ImageSpan imageSpan = new ImageSpan(this, R.drawable.search_icon);
            spannableString.setSpan(imageSpan, index, index + replacement.length(), 0);
            emptyText.setText(spannableString);
        }
    }

    private void newRandomOrder() {
        randomSeriesOrder.clear();
        //noinspection unchecked
        randomSeriesOrder.addAll((List) new Select().from(Series.class)
                .orderBy("RANDOM()")
                .execute());
    }

    private void possiblySelectPosition(int position) {
        if (recyclerView != null) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(position);
            if (viewHolder != null) {
                viewHolder.itemView.setSelected(true);
            }
        }
        if (player != null) {
            SoundUtils.rollDice(player, this);
        }
    }

    private void unselectAllPositions() {
        for (int i=0; i<myShows.size(); i++ ) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.itemView.setSelected(false);
            }
        }
        if (player != null) {
            player.reset();
        }
    }

    @Override
    public void hearShake() {
        randomSeries();
    }
}
