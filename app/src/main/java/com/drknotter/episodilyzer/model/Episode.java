package com.drknotter.episodilyzer.model;

import android.net.Uri;
import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.drknotter.episodilyzer.server.TheTVDBService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by plunkett on 1/29/16.
 */
public class Episode extends Model {
    public Episode() {}

    public Episode(
            com.drknotter.episodilyzer.server.model.Episode episode, Series series, int seasonId) {
        super();
        id = episode.id;
        this.series = series;
        director = episode.director;
        if (episode.thumbHeight > 0
                && (float) episode.thumbWidth / episode.thumbHeight > 13.0 / 9.0) {
            episodeImageFlag = 2;
        } else {
            episodeImageFlag = 1;
        }
        episodeName = episode.episodeName;
        episodeNumber = episode.airedEpisodeNumber;
        firstAired = episode.firstAired;
        guestStars = TextUtils.join("|", episode.guestStars != null
                ? episode.guestStars : new ArrayList());
        overview = episode.overview;
        rating = episode.siteRating;
        ratingCount = episode.siteRatingCount;
        seasonNumber = episode.airedSeason;
        writer = episode.writers != null && episode.writers.size() > 0
                ? episode.writers.get(0) : "";
        absoluteNumber = episode.absoluteNumber;
        filename = episode.filename;
        lastUpdated = episode.lastUpdated;
        this.seasonId = seasonId;
        thumbAdded = episode.thumbAdded;
        thumbHeight = episode.thumbHeight;
        thumbWidth = episode.thumbWidth;
        selected = episode.airedSeason > 0;
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

    // An unsigned integer from 1-6.
    //    1. 4:3 - Indicates an image is a proper 4:3 (1.31 to 1.35) aspect ratio.
    //    2. 16:9 - Indicates an image is a proper 16:9 (1.739 to 1.818) aspect ratio.
    //    3. Invalid Aspect Ratio - Indicates anything not in a 4:3 or 16:9 ratio. We don't bother listing any other non standard ratios.
    //    4. Image too Small - Just means the image is smaller then 300x170.
    //    5. Black Bars - Indicates there are black bars along one or all four sides of the image.
    //    6. Improper Action Shot - Could mean a number of things, usually used when someone uploads a promotional picture that isn't actually from that episode but does refrence the episode, it could also mean it's a credit shot or that there is writting all over it. It's rarely used since most times an image would just be outright deleted if it falls in this category.
    // It can also be null. If it's 1 or 2 the site assumes it's a proper image, anything above 2 is considered incorrect and can be replaced by anyone with an account.
    @Column(name="episodeImageFlag")
    public int episodeImageFlag;

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
    public boolean selected;

    private static final String DEFAULT_NAME = "Untitled";
    public String getName() {
        return TextUtils.isEmpty(episodeName) ? DEFAULT_NAME : episodeName;
    }

    public List<String> directors() {
        return directors(Integer.MAX_VALUE);
    }

    public List<String> directors(int limit) {
        List<String> directors = new ArrayList<>(Arrays.asList(director != null ? director.split("\\|") : new String[0]));
        directors.removeAll(Arrays.asList(null, ""));
        directors = directors.subList(0, Math.min(limit, directors.size()));
        return directors;
    }

    public List<String> writers() {
        return writers(Integer.MAX_VALUE);
    }

    public List<String> writers(int limit) {
        List<String> writers = new ArrayList<>(Arrays.asList(writer != null ? writer.split("\\|") : new String[0]));
        writers.removeAll(Arrays.asList(null, ""));
        writers = writers.subList(0, Math.min(limit, writers.size()));
        return writers;
    }

    public List<String> guestStars() {
        return guestStars(Integer.MAX_VALUE);
    }

    public List<String> guestStars(int limit) {
        List<String> guestStars = new ArrayList<>(Arrays.asList(this.guestStars != null ? this.guestStars.split("\\|") : new String[0]));
        guestStars.removeAll(Arrays.asList(null, ""));
        guestStars = guestStars.subList(0, Math.min(limit, guestStars.size()));
        return guestStars;
    }

    public Uri imageUri() {
        if (filename != null) {
            return Uri.parse(TheTVDBService.WEB_URL)
                    .buildUpon()
                    .appendPath("banners")
                    .appendPath(filename)
                    .build();
        }
        return null;
    }
}
