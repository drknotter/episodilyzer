package com.drknotter.episodilyzer.event;

import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;

public class SeriesSaveFailEvent {
    public enum Reason {
        NO_RESPONSE,
        NETWORK,
        PARSE,
        CANCELLED,
    }
    public SaveSeriesInfo searchResult;
    public Reason reason;
    public String message;

    public SeriesSaveFailEvent(SaveSeriesInfo searchResult, Reason reason, String message) {
        this.searchResult = searchResult;
        this.reason = reason;
        this.message = message;
    }
}
