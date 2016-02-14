package com.drknotter.episodilyzer.view.holder;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Season;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SeasonHeaderViewHolder extends BindableViewHolder<Season> {
    public interface OnSeasonSelectedChangeListener {
        void onSeasonSelectedChange(Season s, boolean selected);
    }

    @Bind(R.id.seasonName)
    TextView seasonName;
    @Bind(R.id.selected)
    CheckBox selected;

    private OnSeasonSelectedChangeListener listener;

    public SeasonHeaderViewHolder(View itemView, OnSeasonSelectedChangeListener listener) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.listener = listener;
    }

    @Override
    public void bind(final Season season) {
        String headerText = season.number > 0
                ? String.format(
                        itemView.getResources().getString(R.string.season_header),
                        season.number)
                : itemView.getResources().getString(R.string.season_special_header);
        seasonName.setText(headerText);

        selected.setOnCheckedChangeListener(null);
        selected.setChecked(season.areAllEpisodesSelected());
        selected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (listener != null) {
                    listener.onSeasonSelectedChange(season, isChecked);
                }
            }
        });
    }
}
