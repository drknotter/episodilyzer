package com.drknotter.episodilyzer.event;

import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;

public class SeriesSaveStartEvent {
    public SaveSeriesInfo searchResult;
    public SeriesSaveStartEvent(SaveSeriesInfo searchResult) {
        this.searchResult = searchResult;
    }
}
