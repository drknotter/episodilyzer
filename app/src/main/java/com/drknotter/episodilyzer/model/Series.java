package com.drknotter.episodilyzer.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.server.model.FullSeries;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Table(name="Series")
public class Series extends Model {

    public Series() {}

    public Series(FullSeries fullSeries) {
        super();
        id = fullSeries.series.id;
        actors = fullSeries.series.actors;
        firstAired = fullSeries.series.firstAired;
        lastUpdated = fullSeries.series.lastUpdated;
        overview = fullSeries.series.overview;
        rating = fullSeries.series.rating;
        ratingCount = fullSeries.series.ratingCount;
        seriesName = fullSeries.series.seriesName;
        status = fullSeries.series.status;
        lastAccessed = System.currentTimeMillis();
    }

    @Column(name = "series_id", index=true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
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

    // Unix time stamp indicating the last time the user accessed this series.
    @Column(name="lastAccessed", index=true)
    public long lastAccessed;

    public SeriesOverview seriesOverview() {
        return new SeriesOverview(this);
    }

    public List<String> starring(int limit) {
        List<String> starring = new ArrayList<>(Arrays.asList(actors != null ? actors.split("\\|") : new String[0]));
        starring.removeAll(Arrays.asList(null, ""));
        starring = starring.subList(0, Math.min(limit, starring.size()));
        return starring;
    }

    public String firstAiredText() {
        try {
            Date firstAiredDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(firstAired);
            return new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).format(firstAiredDate);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Episode> episodes() {
        return new Select()
                .from(Episode.class)
                .where("series = ?", getId())
                .and("seasonNumber > 0")
                .orderBy("seasonNumber, episodeNumber")
                .execute();
    }

    public List<Episode> specialEpisodes() {
        return new Select()
                .from(Episode.class)
                .where("series = ?", getId())
                .and("seasonNumber = 0")
                .orderBy("episodeNumber")
                .execute();
    }

    public Banner bestBanner() {
        return new Select()
                .from(Banner.class)
                .where("series = ?", getId())
                .and("type = ?", Banner.TYPE_SERIES)
                .orderBy("rating DESC")
                .executeSingle();
    }

    public Banner bestFanart() {
        return new Select()
                .from(Banner.class)
                .where("series = ?", getId())
                .and("type = ?", Banner.TYPE_FANART)
                .orderBy("rating DESC")
                .executeSingle();
    }
}
