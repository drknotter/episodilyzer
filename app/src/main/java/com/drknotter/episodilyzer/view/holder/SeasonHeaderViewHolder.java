package com.drknotter.episodilyzer.view.holder;

import android.view.View;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeasonHeaderViewHolder extends BindableViewHolder<Integer> {
    @Bind(R.id.seasonName)
    TextView seasonName;

    public SeasonHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Integer seasonNumber) {
        String headerText = seasonNumber > 0
                ? String.format(
                        itemView.getResources().getString(R.string.season_header),
                        seasonNumber)
                : itemView.getResources().getString(R.string.season_special_header);
        seasonName.setText(headerText);
    }
}
