package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.BriefSeries;
import com.drknotter.episodilyzer.view.holder.BriefSeriesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plunkett on 1/23/16.
 */
public class SearchResultsAdapter extends RecyclerView.Adapter<BriefSeriesViewHolder> {
    private List<BriefSeries> searchResults = new ArrayList<>();

    public SearchResultsAdapter(List<BriefSeries> searchResults) {
        this.searchResults.addAll(searchResults);
    }

    @Override
    public BriefSeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_series, parent, false);
        return new BriefSeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BriefSeriesViewHolder holder, int position) {
        holder.bindSeries(searchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }
}
