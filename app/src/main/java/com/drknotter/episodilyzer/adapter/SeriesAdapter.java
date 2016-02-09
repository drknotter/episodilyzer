package com.drknotter.episodilyzer.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.view.holder.EpisodeViewHolder;
import com.drknotter.episodilyzer.view.holder.SeasonHeaderViewHolder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

public class SeriesAdapter
        extends RecyclerView.Adapter<EpisodeViewHolder>
        implements StickyRecyclerHeadersAdapter<SeasonHeaderViewHolder> {
    private List<Episode> episodes;

    public SeriesAdapter(List<Episode> episodes) {
        this.episodes = episodes;

    }

    @Override
    public EpisodeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_episode, parent, false);
        return new EpisodeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EpisodeViewHolder holder, int position) {
        holder.bind(episodes.get(position));
    }

    @Override
    public long getHeaderId(int position) {
        return episodes.get(position).seasonId;
    }

    @Override
    public SeasonHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.view_season_header, parent, false);
        return new SeasonHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(SeasonHeaderViewHolder holder, int position) {
        holder.bind(episodes.get(position).seasonNumber);
    }

    @Override
    public int getItemCount() {
        if (episodes != null) {
            return episodes.size();
        }
        return 0;
    }
}
