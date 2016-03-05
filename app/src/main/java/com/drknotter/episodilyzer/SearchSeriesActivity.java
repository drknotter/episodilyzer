package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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
import org.greenrobot.eventbus.ThreadMode;

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

public class SearchSeriesActivity extends RecyclerViewActivity {

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
        int index = 0;
        Iterator<SaveSeriesInfo> iterator = searchResults.iterator();
        while (iterator.hasNext()) {
            SaveSeriesInfo info = iterator.next();
            if (info.seriesId == event.series.id) {
                iterator.remove();
                recyclerView.getAdapter().notifyItemRemoved(index);
                break;
            }
            index++;
        }
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
                Snackbar.LENGTH_SHORT).show();
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

    private void onSearchSuccess(List<SaveSeriesInfo> resultList) {
        searchResults.clear();
        searchResults.addAll(resultList);
        recyclerView.getAdapter().notifyDataSetChanged();

        if (searchResults.size() > 0) {
            emptyText.setVisibility(View.GONE);
            emptyImage.setVisibility(View.GONE);
        } else {
            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(R.string.no_search_results);
            emptyImage.setVisibility(View.VISIBLE);
            emptyImage.setImageResource(R.drawable.error);
        }
    }

    private void onSearchFailure(RetrofitError error) {
        searchResults.clear();
        recyclerView.getAdapter().notifyDataSetChanged();

        emptyText.setVisibility(View.VISIBLE);
        emptyImage.setVisibility(View.VISIBLE);
        emptyImage.setImageResource(R.drawable.network_error);
        if (error.isNetworkError()) {
            emptyText.setText(R.string.network_error);
        } else if (error.getResponse() == null) {
            emptyText.setText(R.string.no_response);
        } else if (error.getResponse().getReason() == null) {
            emptyText.setText(R.string.search_failed);
        } else {
            emptyText.setText(getString(R.string.search_failed_with_message, error.getResponse().getReason()));
        }
    }

    private static class SearchResultCallback implements Callback<SearchResult> {
        private WeakReference<SearchSeriesActivity> activityRef;

        SearchResultCallback(SearchSeriesActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @DebugLog
        @Override
        public void success(SearchResult searchResult, Response response) {
            SearchSeriesActivity activity = activityRef.get();
            if (activity != null) {
                List<SaveSeriesInfo> results = new ArrayList<>();
                if (searchResult != null && searchResult.resultList != null) {
                    results.addAll(searchResult.resultList);
                }

                Iterator<SaveSeriesInfo> iterator = results.iterator();
                while (iterator.hasNext()) {
                    SaveSeriesInfo saveSeriesInfo = iterator.next();
                    if (new Select().from(Series.class)
                            .where("series_id = ?", saveSeriesInfo.seriesId)
                            .exists()) {
                        iterator.remove();
                    }
                }

                activity.onSearchSuccess(results);
            }
        }

        @DebugLog
        @Override
        public void failure(RetrofitError error) {
            SearchSeriesActivity activity = activityRef.get();
            if (activity != null) {
                activity.onSearchFailure(error);
            }
        }
    }
}
