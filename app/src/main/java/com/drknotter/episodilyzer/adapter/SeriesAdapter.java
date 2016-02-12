package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.view.holder.BindableViewHolder;
import com.drknotter.episodilyzer.view.holder.EpisodeViewHolder;
import com.drknotter.episodilyzer.view.holder.SeasonHeaderViewHolder;

import java.util.List;

public class SeriesAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_EPISODE = 1;

    private List<Object> seriesInfo;

    public SeriesAdapter(List<Object> seriesInfo) {
        this.seriesInfo = seriesInfo;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch(viewType) {
            case VIEW_TYPE_EPISODE: {
                View view = inflater.inflate(R.layout.view_episode, parent, false);
                return new EpisodeViewHolder(view);
            }

            case VIEW_TYPE_HEADER: {
                View view = inflater.inflate(R.layout.view_season_header, parent, false);
                return new SeasonHeaderViewHolder(view);
            }
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof BindableViewHolder) {
            //noinspection unchecked
            ((BindableViewHolder) holder).bind(seriesInfo.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if (seriesInfo != null) {
            return seriesInfo.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < 0 || position >= seriesInfo.size()) {
            return -1;
        }

        if (seriesInfo.get(position) instanceof Integer) {
            return VIEW_TYPE_HEADER;
        } else if (seriesInfo.get(position) instanceof Episode) {
            return VIEW_TYPE_EPISODE;
        }

        return -1;
    }
}
