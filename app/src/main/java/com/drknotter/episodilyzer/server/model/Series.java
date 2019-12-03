package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by plunkett on 1/25/16.
 */
public class Series {
    // An unsigned integer assigned by our site to the series. It does not change and will always represent the same series. Cannot be null.
    public int id;

    // A string containing the date the series first aired in plain text using the format "YYYY-MM-DD". Can be null.
    public String firstAired;

    // A string containing the overview in the language requested. Will return the English overview if no translation is available in the language requested. Can be null.
    public String overview;

    // The average siteRating our users have rated the series out of 10, rounded to 1 decimal place. Can be null.
    public double siteRating;

    // An unsigned integer representing the number of users who have rated the series. Can be null.
    public int siteRatingCount;

    // A string containing the series name in the language you requested. Will return the English name if no translation is found in the language requested. Can be null if the name isn't known in the requested language or English.
    public String seriesName;

    // A string containing either "Ended" or "Continuing". Can be null.
    public String status;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the highest voted banner for the requested series. Can be null.
    public String banner;

    // Unix time stamp indicating the last time any changes were made to the series. Can be null.
    public long lastUpdated;

}
