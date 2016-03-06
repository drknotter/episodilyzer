package com.drknotter.episodilyzer.server.model;

import com.drknotter.episodilyzer.model.Series;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Series", strict=false)
public class SaveSeriesInfo {
    @Element(name="seriesid")
    public int seriesId;

    @Element(name="language", required=false)
    public String language;

    @Element(name="SeriesName")
    public String seriesName;

    @Element(name="banner", required=false)
    public String banner;

    @Element(name="Overview", required=false)
    public String overview;

    @Element(name="FirstAired", required=false)
    public String firstAired;

    @Element(name="IMDB_ID", required=false)
    public String imdbId;

    public SaveSeriesInfo() {}

    public SaveSeriesInfo(Series series) {
        this(series.id, series.seriesName, series.overview, series.firstAired);
    }

    public SaveSeriesInfo(int seriesId, String seriesName, String overview, String firstAired) {
        this.seriesId = seriesId;
        this.seriesName = seriesName;
        this.overview = overview;
        this.firstAired = firstAired;
    }
}
