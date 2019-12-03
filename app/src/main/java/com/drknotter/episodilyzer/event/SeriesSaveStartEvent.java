package com.drknotter.episodilyzer.event;

import com.drknotter.episodilyzer.server.model.SeriesSearchResult;

public class SeriesSaveStartEvent {
    public SeriesSearchResult searchResult;
    public SeriesSaveStartEvent(SeriesSearchResult searchResult) {
        this.searchResult = searchResult;
    }
}
