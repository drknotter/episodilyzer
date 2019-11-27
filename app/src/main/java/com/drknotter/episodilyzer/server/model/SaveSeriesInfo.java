package com.drknotter.episodilyzer.server.model;

import com.drknotter.episodilyzer.model.Series;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

public class SaveSeriesInfo {
    public String banner;

    public String firstAired;

    public int id;

    public String overview;

    public String seriesName;

    public SaveSeriesInfo() {}

    public SaveSeriesInfo(Series series) {
        this(series.id, series.seriesName, series.overview, series.firstAired);
    }

    public SaveSeriesInfo(int id, String seriesName, String overview, String firstAired) {
        this.id = id;
        this.seriesName = seriesName;
        this.overview = overview;
        this.firstAired = firstAired;
    }
}
