package com.drknotter.episodilyzer.event;

import com.drknotter.episodilyzer.model.Series;

public class SeriesSaveSuccessEvent {
    public Series series;
    public SeriesSaveSuccessEvent(Series series) {
        this.series = series;
    }
}
