package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by plunkett on 1/25/16.
 */
public class Episode {
    // An unsigned integer assigned by our site to the episode. Cannot be null.
    public int id;

    // A pipe delimited string of directors in plain text. Can be null.
    public String director;

    public List<String> directors;

    // An unsigned integer from 1-6. 1. 4:3 - Indicates an image is a proper 4:3 (1.31 to 1.35) aspect ratio. 2. 16:9 - Indicates an image is a proper 16:9 (1.739 to 1.818) aspect ratio. 3. Invalid Aspect Ratio - Indicates anything not in a 4:3 or 16:9 ratio. We don't bother listing any other non standard ratios. 4. Image too Small - Just means the image is smaller then 300x170. 5. Black Bars - Indicates there are black bars along one or all four sides of the image. 6. Improper Action Shot - Could mean a number of things, usually used when someone uploads a promotional picture that isn't actually from that episode but does refrence the episode, it could also mean it's a credit shot or that there is writting all over it. It's rarely used since most times an image would just be outright deleted if it falls in this category. It can also be null. If it's 1 or 2 the site assumes it's a proper image, anything above 2 is considered incorrect and can be replaced by anyone with an account.
//    public int epImgFlag;

    // A string containing the episode name in the language requested. Will return the English name if no translation is available in the language requested.
    public String episodeName;

    // An unsigned integer representing the episode number in its season according to the aired order. Cannot be null.
    public int airedEpisodeNumber;

    // A string containing the date the series first aired in plain text using the format "YYYY-MM-DD". Can be null.
    public String firstAired;

    // A pipe delimited string of guest stars in plain text. Can be null.
    public List<String> guestStars;

    // A string containing the overview in the language requested. Will return the English overview if no translation is available in the language requested. Can be null.
    public String overview;

    // The average siteRating our users have rated the series out of 10, rounded to 1 decimal place. Can be null.
    public float siteRating;

    // An unsigned integer representing the number of users who have rated the series. Can be null.
    public int siteRatingCount;

    // An unsigned integer representing the season number for the episode according to the aired order. Cannot be null.
    public int airedSeason;

    // A pipe delimited string of writers in plain text. Can be null.
    public List<String> writers;

    // An unsigned integer. Can be null. Indicates the absolute episode number and completely ignores seasons. In others words a series with 20 episodes per season will have Season 3 episode 10 listed as 50. The field is mostly used with cartoons and anime series as they may have ambiguous seasons making it easier to use this field.
    public int absoluteNumber;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the location of the episode image. Can be null.
    public String filename;

    // Unix time stamp indicating the last time any changes were made to the episode. Can be null.
    public long lastUpdated;

    // An unsigned integer assigned by our site to the season. Cannot be null.
//    public int seasonId;

    // A string containing the time the episode image was added to our site in the format "YYYY-MM-DD HH:MM:SS" based on a 24 hour clock. Can be null.
    public String thumbAdded;

    // An unsigned integer that represents the height of the episode image in pixels. Can be null
    public int thumbHeight;

    // An unsigned integer that represents the width of the episode image in pixels. Can be null
    public int thumbWidth;
}
