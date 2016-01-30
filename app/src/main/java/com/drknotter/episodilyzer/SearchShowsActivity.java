package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.drknotter.episodilyzer.adapter.SearchResultsAdapter;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.SearchResult;
import com.drknotter.episodilyzer.server.model.BriefSeries;

import java.lang.ref.WeakReference;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;

public class SearchShowsActivity extends AppCompatActivity {

    private TheTVDBService theTVDBService;

    @Bind(R.id.search_results_list)
    RecyclerView searchResultsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shows);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        searchResultsList.setLayoutManager(new LinearLayoutManager(this));

        theTVDBService = new RestAdapter.Builder()
                .setEndpoint(TheTVDBService.BASE_URL)
                .setConverter(new SimpleXMLConverter())
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build()
                .create(TheTVDBService.class);

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

            theTVDBService.searchShows(query, new SearchResultCallback(this));
        }
    }

    private void onSearchSuccess(List<BriefSeries> resultList) {
        searchResultsList.setAdapter(new SearchResultsAdapter(resultList));
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
