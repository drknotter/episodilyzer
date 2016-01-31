package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.drknotter.episodilyzer.adapter.MyShowsAdapter;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.utils.SeriesUtils;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EpisodilyzerActivity extends AppCompatActivity implements SeriesUtils.OnSavedSeriesFetchedListener {
    @Bind(R.id.my_shows_list)
    RecyclerView myShowsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episodilyzer);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myShowsList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

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
        myShowsList.setAdapter(adapter);
    }
}
