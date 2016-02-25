package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.adapter.EpisodilyzerAdapter;
import com.drknotter.episodilyzer.fragment.AboutDialogFragment;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.utils.SeriesUtils;
import com.drknotter.episodilyzer.view.smoothscroller.CenteredSmoothScroller;
import com.squareup.seismic.ShakeDetector;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import butterknife.OnClick;

public class EpisodilyzerActivity extends RecyclerViewActivity implements ShakeDetector.Listener {
    private List<Series> myShows = new ArrayList<>();

    private Queue<Series> randomSeriesOrder = new ArrayDeque<>();
    private Integer randomSeriesPosition = null;

    private ShakeDetector shakeDetector;

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
    protected void onResume() {
        super.onResume();
        myShows.clear();
        myShows.addAll(SeriesUtils.allSeries());
        recyclerView.getAdapter().notifyDataSetChanged();

        if (shakeDetector == null) {
            shakeDetector = new ShakeDetector(this);
        }
        shakeDetector.start((SensorManager) getSystemService(SENSOR_SERVICE));
    }

    @Override
    protected void onPause() {
        super.onPause();
        shakeDetector.stop();
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

    @OnClick(R.id.fab)
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
        } else {
            newRandomOrder();
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
    }

    private void unselectAllPositions() {
        for (int i=0; i<myShows.size(); i++ ) {
            RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForAdapterPosition(i);
            if (viewHolder != null) {
                viewHolder.itemView.setSelected(false);
            }
        }
    }

    @Override
    public void hearShake() {
        randomSeries();
    }
}
