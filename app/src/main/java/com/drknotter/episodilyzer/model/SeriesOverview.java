package com.drknotter.episodilyzer.model;

import java.util.List;

public class SeriesOverview {
    public SeriesOverview(Series series) {
        this.overview = series.overview;
        this.starring = series.starring(5);
        this.firstAired = series.firstAiredText();
    }
    public String overview;
    public List<String> starring;
    public String firstAired;
}
