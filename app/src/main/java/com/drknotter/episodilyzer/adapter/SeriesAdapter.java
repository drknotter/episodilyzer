package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class SeriesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Object> seriesInfo;

    public SeriesAdapter(List<Object> seriesInfo) {
        this.seriesInfo = seriesInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
