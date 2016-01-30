package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.FullSeries;
import com.drknotter.episodilyzer.view.holder.FullSeriesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plunkett on 1/26/16.
 */
public class MyShowsAdapter extends RecyclerView.Adapter<FullSeriesViewHolder> {
    private List<FullSeries> myShows = new ArrayList<>();

    public MyShowsAdapter(List<FullSeries> myShows) {
        this.myShows.addAll(myShows);
    }

    @Override
    public FullSeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_series, parent, false);
        return new FullSeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FullSeriesViewHolder holder, int position) {
        holder.bindSeries(myShows.get(position));
    }

    @Override
    public int getItemCount() {
        return myShows.size();
    }
}