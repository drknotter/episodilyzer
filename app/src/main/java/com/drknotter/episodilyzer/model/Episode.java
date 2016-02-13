package com.drknotter.episodilyzer.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.drknotter.episodilyzer.server.model.BaseEpisode;

/**
 * Created by plunkett on 1/29/16.
 */
public class Episode extends Model {
    public Episode() {}

    public Episode(BaseEpisode baseEpisode, Series series) {
        super();
        id = baseEpisode.id;
        this.series = series;
        director = baseEpisode.director;
        episodeName = baseEpisode.episodeName;
        episodeNumber = baseEpisode.episodeNumber;
        firstAired = baseEpisode.firstAired;
        guestStars = baseEpisode.guestStars;
        overview = baseEpisode.overview;
        rating = baseEpisode.rating;
        ratingCount = baseEpisode.ratingCount;
        seasonNumber = baseEpisode.seasonNumber;
        writer = baseEpisode.writer;
        absoluteNumber = baseEpisode.absoluteNumber;
        filename = baseEpisode.filename;
        lastUpdated = baseEpisode.lastUpdated;
        seasonId = baseEpisode.seasonId;
        thumbAdded = baseEpisode.thumbAdded;
        thumbHeight = baseEpisode.thumbHeight;
        thumbWidth = baseEpisode.thumbWidth;
    }

    // An unsigned integer assigned by our site to the episode. Cannot be null.
    @Column(name="episode_id", index = true, unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public int id;

    // Series that this episode is associated with.
    @Column(name="series", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Series series;

    // A pipe delimited string of directors in plain text. Can be null.
    @Column(name="director", index = true)
    public String director;

    // A string containing the episode name in the language requested. Will return the English name if no translation is available in the language requested.
    @Column(name="episodeName", index = true)
    public String episodeName;

    // An unsigned integer representing the episode number in its season according to the aired order. Cannot be null.
    @Column(name="episodeNumber", index = true)
    public int episodeNumber;

    // A string containing the date the series first aired in plain text using the format "YYYY-MM-DD". Can be null.
    @Column(name="firstAired")
    public String firstAired;

    // A pipe delimited string of guest stars in plain text. Can be null.
    @Column(name="guestStars")
    public String guestStars;

    // A string containing the overview in the language requested. Will return the English overview if no translation is available in the language requested. Can be null.
    @Column(name="overview")
    public String overview;

    // The average rating our users have rated the series out of 10, rounded to 1 decimal place. Can be null.
    @Column(name="rating", index = true)
    public float rating;

    // An unsigned integer representing the number of users who have rated the series. Can be null.
    @Column(name="ratingCount", index = true)
    public int ratingCount;

    // An unsigned integer representing the season number for the episode according to the aired order. Cannot be null.
    @Column(name="seasonNumber", index = true)
    public int seasonNumber;

    // A pipe delimited string of writers in plain text. Can be null.
    @Column(name="writer", index = true)
    public String writer;

    // An unsigned integer. Can be null. Indicates the absolute episode number and completely ignores seasons. In others words a series with 20 episodes per season will have Season 3 episode 10 listed as 50. The field is mostly used with cartoons and anime series as they may have ambiguous seasons making it easier to use this field.
    @Column(name="absoluteNumber")
    public int absoluteNumber;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the location of the episode image. Can be null.
    @Column(name="filename")
    public String filename;

    // Unix time stamp indicating the last time any changes were made to the episode. Can be null.
    @Column(name="lastupdated", index = true)
    public long lastUpdated;

    // An unsigned integer assigned by our site to the season. Cannot be null.
    @Column(name="seasonid")
    public int seasonId;

    // A string containing the time the episode image was added to our site in the format "YYYY-MM-DD HH:MM:SS" based on a 24 hour clock. Can be null.
    @Column(name="thumb_added")
    public String thumbAdded;

    // An unsigned integer that represents the height of the episode image in pixels. Can be null
    @Column(name="thumb_height")
    public int thumbHeight;

    // An unsigned integer that represents the width of the episode image in pixels. Can be null
    @Column(name="thumb_width")
    public int thumbWidth;

    @Column(name="selected")
    public boolean selected = true;
}
