package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.adapter.SearchShowsAdapter;
import com.drknotter.episodilyzer.event.SeriesSaveFailEvent;
import com.drknotter.episodilyzer.event.SeriesSaveStartEvent;
import com.drknotter.episodilyzer.event.SeriesSaveSuccessEvent;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.server.model.SearchResult;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import hugo.weaving.DebugLog;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;

public class SearchShowsActivity extends RecyclerViewActivity {

    private TheTVDBService theTVDBService;
    private List<SaveSeriesInfo> searchResults = new ArrayList<>();

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

        recyclerView.setAdapter(new SearchShowsAdapter(searchResults));
        handleIntent(getIntent());
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
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
    }

    @Override
    protected RecyclerView.ItemDecoration getItemDecoration() {
        return null;
    }

    @Subscribe
    public void onSeriesSaveStartEvent(SeriesSaveStartEvent event) {
        Snackbar.make(recyclerView,
                getString(
                        R.string.snack_saving_series,
                        event.searchResult.seriesName),
                Snackbar.LENGTH_SHORT).show();
    }

    @Subscribe
    public void onSeriesSaveSuccessEvent(SeriesSaveSuccessEvent event) {
        Snackbar.make(recyclerView, getString(R.string.snack_series_saved, event.series.seriesName), Snackbar.LENGTH_SHORT).show();
        for (int i=0; i<searchResults.size(); i++) {
            if (searchResults.get(i).seriesId == event.series.id) {
                searchResults.remove(i);
                recyclerView.getAdapter().notifyItemRemoved(i);
                break;
            }
        }
    }

    @Subscribe
    public void onSeriesSaveFailEvent(SeriesSaveFailEvent event) {
        Snackbar.make(recyclerView, getString(R.string.snack_series_save_failed, event.searchResult.seriesName), Snackbar.LENGTH_SHORT).show();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            //noinspection ConstantConditions
            getSupportActionBar().setTitle(query);

            searchResults.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
            theTVDBService.searchShows(query, new SearchResultCallback(this));
        }
    }

    @DebugLog
    private void onSearchSuccess(List<SaveSeriesInfo> resultList) {
        searchResults.clear();
        searchResults.addAll(resultList);
        recyclerView.setAdapter(new SearchShowsAdapter(resultList));
    }

    @DebugLog
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
                if (searchResult != null && searchResult.resultList != null) {
                    Iterator<SaveSeriesInfo> iterator = searchResult.resultList.iterator();
                    while (iterator.hasNext()) {
                        SaveSeriesInfo saveSeriesInfo = iterator.next();
                        if (new Select().from(Series.class)
                                .where("series_id = ?", saveSeriesInfo.seriesId)
                                .exists()) {
                            iterator.remove();
                        }
                    }
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
