package com.drknotter.episodilyzer.server.model;

import com.drknotter.episodilyzer.model.Series;

public class SeriesSearchResult {
    public String banner;

    public String firstAired;

    public int id;

    public String overview;

    public String seriesName;

    public SeriesSearchResult() {}

    public SeriesSearchResult(Series series) {
        this(series.id, series.seriesName, series.overview, series.firstAired);
    }

    public SeriesSearchResult(int id, String seriesName, String overview, String firstAired) {
        this.id = id;
        this.seriesName = seriesName;
        this.overview = overview;
        this.firstAired = firstAired;
    }
}
