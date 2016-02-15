package com.drknotter.episodilyzer.model;

public class SeriesOverview {
    public SeriesOverview(String overview, String actors) {
        this.overview = overview;
        this.actors = actors.split("|");
    }
    public String overview;
    public String[] actors;
}
