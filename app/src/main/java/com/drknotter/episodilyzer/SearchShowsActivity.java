package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.drknotter.episodilyzer.adapter.SearchResultsAdapter;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.BriefSeries;
import com.drknotter.episodilyzer.server.model.SearchResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;

public class SearchShowsActivity extends SeriesListActivity {

    private TheTVDBService theTVDBService;
    private List<BriefSeries> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        theTVDBService = new RestAdapter.Builder()
                .setEndpoint(TheTVDBService.BASE_URL)
                .setConverter(new SimpleXMLConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(TheTVDBService.class);

        seriesListView.setAdapter(new SearchResultsAdapter(searchResults));
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //noinspection ConstantConditions
            getSupportActionBar().setTitle(query);

            searchResults.clear();
            seriesListView.getAdapter().notifyDataSetChanged();
            theTVDBService.searchShows(query, new SearchResultCallback(this));
        }
    }

    private void onSearchSuccess(List<BriefSeries> resultList) {
        searchResults.clear();
        searchResults.addAll(resultList);
        seriesListView.setAdapter(new SearchResultsAdapter(resultList));
    }

    private void onSearchFailure() {

    }

    private static class SearchResultCallback implements Callback<SearchResult> {
        private WeakReference<SearchShowsActivity> activityRef;

        SearchResultCallback(SearchShowsActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void success(SearchResult searchResult, Response response) {
            SearchShowsActivity activity = activityRef.get();
            if (activity != null) {
                if (searchResult != null && searchResult.resultList != null && searchResult.resultList.size() > 0) {
                    activity.onSearchSuccess(searchResult.resultList);
                } else {
                    activity.onSearchFailure();
                }
            }
        }

        @Override
        public void failure(RetrofitError error) {
            SearchShowsActivity activity = activityRef.get();
            if (activity != null) {
                activity.onSearchFailure();
            }
        }
    }
}
