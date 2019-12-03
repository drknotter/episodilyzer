package com.drknotter.episodilyzer.adapter;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.SeriesSearchResult;
import com.drknotter.episodilyzer.view.holder.SeriesSearchResultViewHolder;

import java.util.List;

/**
 * Created by plunkett on 1/23/16.
 */
public class SearchShowsAdapter extends RecyclerView.Adapter<SeriesSearchResultViewHolder> {
    private List<SeriesSearchResult> searchResults;

    public SearchShowsAdapter(List<SeriesSearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public SeriesSearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_series_search_result, parent, false);
        return new SeriesSearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SeriesSearchResultViewHolder holder, int position) {
        holder.bindSearchResult(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }
}
