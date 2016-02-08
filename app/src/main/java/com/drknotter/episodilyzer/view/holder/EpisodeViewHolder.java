package com.drknotter.episodilyzer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.drknotter.episodilyzer.model.Episode;

import butterknife.ButterKnife;

public class EpisodeViewHolder extends RecyclerView.ViewHolder {
    public EpisodeViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(Episode episode) {

    }
}
