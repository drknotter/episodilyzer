package com.drknotter.episodilyzer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FullSeriesViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.banner)
    ImageView banner;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.overview)
    TextView overview;
    @Bind(R.id.star_toggle)
    View starToggle;

    public FullSeriesViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

}