package com.drknotter.episodilyzer.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.List;

@Table(name="Series")
public class Series extends Model {
    @Column(name="id", index=true)
    public int id;

    // A pipe delimited string of actors in plain text. Begins and ends with a pipe even if no actors are listed. Cannot be null.
    @Column(name="actors")
    public String actors;

    // A string containing the date the series first aired in plain text using the format "YYYY-MM-DD". Can be null.
    @Column(name="firstAired")
    public String firstAired;

    // A string containing the overview in the language requested. Will return the English overview if no translation is available in the language requested. Can be null.
    @Column(name="overview")
    public String overview;

    // The average rating our users have rated the series out of 10, rounded to 1 decimal place. Can be null.
    @Column(name="rating", index=true)
    public double rating;

    // An unsigned integer representing the number of users who have rated the series. Can be null.
    @Column(name="ratingCount")
    public int ratingCount;

    // A string containing the series name in the language you requested. Will return the English name if no translation is found in the language requested. Can be null if the name isn't known in the requested language or English.
    @Column(name="seriesName", index=true)
    public String seriesName;

    // A string containing either "Ended" or "Continuing". Can be null.
    @Column(name="status")
    public String status;

    // Unix time stamp indicating the last time any changes were made to the series. Can be null.
    @Column(name="lastupdated", index=true)
    public long lastUpdated;

    public List<Episode> episodes() {
        return getMany(Episode.class, "series");
    }

    public List<Banner> banners() {
        return getMany(Banner.class, "series");
    }

    public static class Builder {
        private Series series;

        public Builder() {
            series = new Series();
        }

        public Builder id(int id) {
            series.id = id;
            return this;
        }

        public Builder actors(String actors) {
            series.actors = actors;
            return this;
        }

        public Builder firstAired(String firstAired) {
            series.firstAired = firstAired;
            return this;
        }

        public Builder overview(String overview) {
            series.overview = overview;
            return this;
        }

        public Builder rating(double rating) {
            series.rating = rating;
            return this;
        }

        public Builder ratingCount(int ratingCount) {
            series.ratingCount = ratingCount;
            return this;
        }

        public Builder seriesName(String seriesName) {
            series.seriesName = seriesName;
            return this;
        }

        public Builder status(String status) {
            series.status = status;
            return this;
        }

        public Builder lastUpdated(long lastUpdated) {
            series.lastUpdated = lastUpdated;
            return this;
        }

        public Series build() {
            return series;
        }
    }
}
