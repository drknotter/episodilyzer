package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.drknotter.episodilyzer.adapter.MyShowsAdapter;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.util.ArrayList;
import java.util.List;

public class EpisodilyzerActivity extends RecyclerViewActivity implements SeriesUtils.OnSeriesChangeListener {
    private List<Series> myShows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView.setAdapter(new MyShowsAdapter(myShows));
    }

    @Override
    protected void onResume() {
        super.onResume();
        SeriesUtils.registerOnSeriesChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SeriesUtils.unregisterOnSeriesChangeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_episodilyzer, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    ///////////////////////////////////////////
    // OnSeriesChangeListener implementation //
    ///////////////////////////////////////////

    @Override
    public void onSeriesInserted(List<Series> insertedSeries) {
        if (myShows.addAll(0, insertedSeries)) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onSeriesDeleted(List<Series> deletedSeries) {
        if (myShows.removeAll(deletedSeries)) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onSeriesUpdated(List<Series> updatedSeries) {
        if (myShows.removeAll(updatedSeries)
                || myShows.addAll(0, updatedSeries)) {
            recyclerView.getAdapter().notifyDataSetChanged();
        }
    }
}
