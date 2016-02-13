package com.drknotter.episodilyzer.view.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Season;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeasonHeaderViewHolder extends BindableViewHolder<Season> {
    @Bind(R.id.seasonName)
    TextView seasonName;
    @Bind(R.id.selected)
    CheckBox selected;

    public SeasonHeaderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void bind(Season season) {
        String headerText = season.number > 0
                ? String.format(
                        itemView.getResources().getString(R.string.season_header),
                        season.number)
                : itemView.getResources().getString(R.string.season_special_header);
        seasonName.setText(headerText);

        selected.setChecked(season.areAllEpisodesSelected());
    }
}
