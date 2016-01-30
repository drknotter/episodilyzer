package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="Series", strict=false)
public class BriefSeries {
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
}
