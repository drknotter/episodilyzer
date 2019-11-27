package com.drknotter.episodilyzer;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.RotateAnimation;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drknotter.episodilyzer.adapter.SearchShowsAdapter;
import com.drknotter.episodilyzer.event.SeriesSaveFailEvent;
import com.drknotter.episodilyzer.event.SeriesSaveStartEvent;
import com.drknotter.episodilyzer.event.SeriesSaveSuccessEvent;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.server.model.SearchResult;
import com.drknotter.episodilyzer.server.task.SearchSeriesTask;
import com.drknotter.episodilyzer.server.task.TaskCallback;
import com.google.android.material.snackbar.Snackbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SearchSeriesActivity extends RecyclerViewActivity {
    private static final String TAG = SearchSeriesActivity.class.getSimpleName();

    private List<SaveSeriesInfo> searchResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView.setAdapter(new SearchShowsAdapter(searchResults));
        emptyImage.setImageResource(R.mipmap.app_icon);
        emptyText.setText(R.string.searching);

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
        super.onNewIntent(intent);
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
            if (info.id == event.series.id) {
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

            emptyText.setVisibility(View.VISIBLE);
            emptyText.setText(R.string.searching);

            emptyImage.setVisibility(View.VISIBLE);
            emptyImage.clearAnimation();
            emptyImage.startAnimation(new SearchAnimation());

            new SearchSeriesTask(query, new SearchResultTaskCallback(this)).execute();
        }
    }

    private void onSearchSuccess(List<SaveSeriesInfo> resultList) {
        searchResults.clear();
        searchResults.addAll(resultList);
        recyclerView.getAdapter().notifyDataSetChanged();

        emptyImage.clearAnimation();

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

    private void onSearchFailure(String message) {
        Log.d(TAG, "onSearchFailure(" + message + ")");
        searchResults.clear();
        recyclerView.getAdapter().notifyDataSetChanged();

        emptyImage.clearAnimation();

        emptyText.setVisibility(View.VISIBLE);
        emptyImage.setVisibility(View.VISIBLE);
        emptyImage.setImageResource(R.drawable.network_error);
        emptyText.setText(message);
    }

    private static class SearchResultTaskCallback implements TaskCallback<SearchResult> {
        private final WeakReference<SearchSeriesActivity> activityRef;

        SearchResultTaskCallback(SearchSeriesActivity activity) {
            activityRef = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(SearchResult result) {
            SearchSeriesActivity activity = activityRef.get();
            if (activity != null) {
                activity.onSearchSuccess(result.data);
            }
        }

        @Override
        public void onError(String errorMessage) {
            SearchSeriesActivity activity = activityRef.get();
            if (activity != null) {
                activity.onSearchFailure(errorMessage == null || errorMessage.isEmpty()
                        ? Episodilyzer.getInstance().getString(R.string.search_failed)
                        : errorMessage);
            }
        }
    }

    private static class SearchAnimation extends RotateAnimation {
        SearchAnimation() {
            super(0f, 360f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.583333333f);
            setRepeatCount(RotateAnimation.INFINITE);
            setDuration(1000);
            setInterpolator(new AnticipateOvershootInterpolator());
        }
    }
}
