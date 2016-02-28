package com.drknotter.episodilyzer.event;

import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;

public class SeriesSaveFailEvent {
    public SaveSeriesInfo searchResult;
    public SeriesSaveFailEvent(SaveSeriesInfo searchResult) {
        this.searchResult = searchResult;
    }
}
