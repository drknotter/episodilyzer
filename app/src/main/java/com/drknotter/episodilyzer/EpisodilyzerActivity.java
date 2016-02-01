package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;

import com.drknotter.episodilyzer.adapter.MyShowsAdapter;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.util.List;

public class EpisodilyzerActivity extends SeriesListActivity implements SeriesUtils.OnSavedSeriesFetchedListener {
    @Override
    protected void onResume() {
        super.onResume();
        SeriesUtils.fetchSavedSeries(this);
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

    @Override
    public void onSavedSeriesFetched(List<Series> savedSeries) {
        MyShowsAdapter adapter = new MyShowsAdapter(savedSeries);
        seriesList.setAdapter(adapter);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        findViewById(R.id.toolbar).requestLayout();
    }
}
