package com.drknotter.episodilyzer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeasonHeaderViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.seasonName)
    TextView seasonName;

    public SeasonHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void bind(int seasonNumber) {
        String headerText = seasonNumber > 0
                ? String.format(
                        itemView.getResources().getString(R.string.season_header),
                        seasonNumber)
                : itemView.getResources().getString(R.string.season_special_header);
        seasonName.setText(headerText);
    }
}
