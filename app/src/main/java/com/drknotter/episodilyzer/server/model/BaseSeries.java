package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by plunkett on 1/25/16.
 */
@Root(name = "Series", strict = false)
public class BaseSeries {
    // An unsigned integer assigned by our site to the series. It does not change and will always represent the same series. Cannot be null.
    @Element(name="id")
    public int id;

    // A pipe delimited string of actors in plain text. Begins and ends with a pipe even if no actors are listed. Cannot be null.
    @Element(required = false, name="Actors")
    public String actors;

    // The full name in English for the day of the week the series airs in plain text. Can be null.
    @Element(required = false, name="Airs_DayOfWeek")
    public String airsDayOfWeek;

    // A string indicating the time of day the series airs on its original network. Format "HH:MM AM/PM". Can be null.
    @Element(required = false, name="Airs_Time")
    public String airsTime;

    // The rating given to the series based on the US rating system. Can be null or a 4-5 character string.
    @Element(required = false, name="ContentRating")
    public String contentRating;

    // A string containing the date the series first aired in plain text using the format "YYYY-MM-DD". Can be null.
    @Element(required = false, name="FirstAired")
    public String firstAired;

    // Pipe delimited list of genres in plain text. Begins and ends with a | but may also be null.
    @Element(required = false, name="Genre")
    public String genre;

    // An alphanumeric string containing the IMDB ID for the series. Can be null.
    @Element(required = false, name="IMDB_ID")
    public String imdbId;

    // A two character string indicating the language in accordance with ISO-639-1. Cannot be null.
    @Element(name="Language")
    public String language;

    // A string containing the network name in plain text. Can be null.
    @Element(required = false, name="Network")
    public String network;

    // Not in use, will be an unsigned integer if ever used. Can be null.
    @Element(required = false, name="NetworkID")
    public int networkID;

    // A string containing the overview in the language requested. Will return the English overview if no translation is available in the language requested. Can be null.
    @Element(required = false, name="Overview")
    public String overview;

    // The average rating our users have rated the series out of 10, rounded to 1 decimal place. Can be null.
    @Element(required = false, name="Rating")
    public double rating;

    // An unsigned integer representing the number of users who have rated the series. Can be null.
    @Element(required = false, name="RatingCount")
    public int ratingCount;

    // An unsigned integer representing the runtime of the series in minutes. Can be null.
    @Element(required = false, name="Runtime")
    public int runtime;

    // Deprecated. An unsigned integer representing the series ID at tv.com. As TV.com now only uses these ID's internally it's of little use and no longer updated. Can be null.
    @Element(required = false, name="SeriesID")
    public int seriesId;

    // A string containing the series name in the language you requested. Will return the English name if no translation is found in the language requested. Can be null if the name isn't known in the requested language or English.
    @Element(required = false, name="SeriesName")
    public String seriesName;

    // A string containing either "Ended" or "Continuing". Can be null.
    @Element(required = false, name="Status")
    public String status;

    // A string containing the date/time the series was added to our site in the format "YYYY-MM-DD HH:MM:SS" based on a 24 hour clock. Is null for older series.
    @Element(required = false)
    public String added;

    // An unsigned integer. The ID of the user on our site who added the series to our database. Is null for older series.
    @Element(required = false)
    public int addedBy;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the highest voted banner for the requested series. Can be null.
    @Element(required = false)
    public String banner;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the highest voted fanart for the requested series. Can be null.
    @Element(required = false)
    public String fanart;

    // Unix time stamp indicating the last time any changes were made to the series. Can be null.
    @Element(required = false, name="lastupdated")
    public long lastUpdated;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the highest voted poster for the requested series. Can be null.
    @Element(required = false)
    public String posters;

    // An alphanumeric string containing the zap2it id. Can be null.
    @Element(required = false, name="zap2it_id")
    public String zap2itId;
}
