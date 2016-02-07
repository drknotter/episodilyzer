package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.view.holder.BriefSeriesViewHolder;

import java.util.List;

/**
 * Created by plunkett on 1/26/16.
 */
public class MyShowsAdapter extends RecyclerView.Adapter<BriefSeriesViewHolder> {
    private List<Series> myShows;

    public MyShowsAdapter(List<Series> myShows) {
        this.myShows = myShows;
    }

    @Override
    public BriefSeriesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_series, parent, false);
        return new BriefSeriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final BriefSeriesViewHolder holder, int position) {
        holder.bindSeries(myShows.get(position),
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition != RecyclerView.NO_POSITION) {
                            myShows.remove(adapterPosition);
                            notifyItemRemoved(adapterPosition);
                        }
                    }
                });
    }

    @Override
    public int getItemCount() {
        return myShows.size();
    }

    @Override
    public long getItemId(int position) {
        return myShows.get(position).id;
    }
}