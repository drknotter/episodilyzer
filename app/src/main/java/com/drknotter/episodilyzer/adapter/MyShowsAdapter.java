package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.view.holder.BriefSeriesViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by plunkett on 1/26/16.
 */
public class MyShowsAdapter extends RecyclerView.Adapter<BriefSeriesViewHolder> {
    private List<Series> myShows = new ArrayList<>();

    public MyShowsAdapter(List<Series> myShows) {
        this.myShows.addAll(myShows);
    }

    @Override
    public BriefSeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_series, parent, false);
        return new BriefSeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BriefSeriesViewHolder holder, int position) {
        holder.bindSeries(myShows.get(position));
    }

    @Override
    public int getItemCount() {
        return myShows.size();
    }
}