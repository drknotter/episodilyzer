package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by plunkett on 1/25/16.
 */
@Root(name="Episode", strict = false)
public class BaseEpisode {
    // An unsigned integer assigned by our site to the episode. Cannot be null.
    public int id;

    // An unsigned integer or decimal. Cannot be null. This returns the value of DVD_episodenumber if that field is not null. Otherwise it returns the value from EpisodeNumber. The field can be used as a simple way of prioritizing DVD order over aired order in your program. In general it's best to avoid using this field as you can accomplish the same task locally and have more control if you use the DVD_episodenumber and EpisodeNumber fields separately.
    @Element(name="Combined_episodenumber")
    public float combinedEpisodeNumber;

    // An unsigned integer or decimal. Cannot be null. This returns the value of DVD_season if that field is not null. Otherwise it returns the value from SeasonNumber. The field can be used as a simple way of prioritizing DVD order over aired order in your program. In general it's best to avoid using this field as you can accomplish the same task locally and have more control if you use the DVD_season and SeasonNumber fields separately.
    @Element(name="Combined_season")
    public float combinedSeason;

    // Deprecated, was meant to be used to aid in scrapping of actual DVD's but has never been populated properly. Any information returned in this field shouldn't be trusted. Will usually be null.
    @Element(required = false, name="DVD_chapter")
    public String dvdChapter;

    // Deprecated, was meant to be used to aid in scrapping of actual DVD's but has never been populated properly. Any information returned in this field shouldn't be trusted. Will usually be null.
    @Element(required = false, name="DVD_discid")
    public String dvdDiscId;

    // A decimal with one decimal and can be used to join episodes together. Can be null, usually used to join episodes that aired as two episodes but were released on DVD as a single episode. If you see an episode 1.1 and 1.2 that means both records should be combined to make episode 1. Cartoons are also known to combine up to 9 episodes together, for example Animaniacs season two.
    @Element(required = false, name="DVD_episodenumber")
    public float dvdEpisodeNumber;

    // An unsigned integer indicating the season the episode was in according to the DVD release. Usually is the same as EpisodeNumber but can be different.
    @Element(required = false, name="DVD_season")
    public int dvdSeason;

    // A pipe delimited string of directors in plain text. Can be null.
    @Element(required = false, name="Director")
    public String directory;

    // An unsigned integer from 1-6. 1. 4:3 - Indicates an image is a proper 4:3 (1.31 to 1.35) aspect ratio. 2. 16:9 - Indicates an image is a proper 16:9 (1.739 to 1.818) aspect ratio. 3. Invalid Aspect Ratio - Indicates anything not in a 4:3 or 16:9 ratio. We don't bother listing any other non standard ratios. 4. Image too Small - Just means the image is smaller then 300x170. 5. Black Bars - Indicates there are black bars along one or all four sides of the image. 6. Improper Action Shot - Could mean a number of things, usually used when someone uploads a promotional picture that isn't actually from that episode but does refrence the episode, it could also mean it's a credit shot or that there is writting all over it. It's rarely used since most times an image would just be outright deleted if it falls in this category. It can also be null. If it's 1 or 2 the site assumes it's a proper image, anything above 2 is considered incorrect and can be replaced by anyone with an account.
    @Element(required = false, name="EpImgFlag")
    public int epImgFlag;

    // A string containing the episode name in the language requested. Will return the English name if no translation is available in the language requested.
    @Element(name="EpisodeName")
    public String episodeName;

    // An unsigned integer representing the episode number in its season according to the aired order. Cannot be null.
    @Element(name="EpisodeNumber")
    public int episodeNumber;

    // A string containing the date the series first aired in plain text using the format "YYYY-MM-DD". Can be null.
    @Element(required = false, name="FirstAired")
    public String firstAired;

    // A pipe delimited string of guest stars in plain text. Can be null.
    @Element(required = false, name="GuestStars")
    public String guestStars;

    // An alphanumeric string containing the IMDB ID for the series. Can be null.
    @Element(required = false, name="IMDB_ID")
    public String imdbId;

    // A two character string indicating the language in accordance with ISO-639-1. Cannot be null.
    @Element(name="Language")
    public String language;

    // A string containing the overview in the language requested. Will return the English overview if no translation is available in the language requested. Can be null.
    @Element(required = false, name="Overview")
    public String overview;

    // An alphanumeric string. Can be null.
    @Element(required = false, name="ProductionCode")
    public String productionCode;

    // The average rating our users have rated the series out of 10, rounded to 1 decimal place. Can be null.
    @Element(required = false, name="Rating")
    public float rating;

    // An unsigned integer representing the number of users who have rated the series. Can be null.
    @Element(required = false, name="RatingCount")
    public int ratingCount;

    // An unsigned integer representing the season number for the episode according to the aired order. Cannot be null.
    @Element(name="SeasonNumber")
    public int seasonNumber;

    // A pipe delimited string of writers in plain text. Can be null.
    @Element(required = false, name="Writer")
    public String writer;

    // An unsigned integer. Can be null. Indicates the absolute episode number and completely ignores seasons. In others words a series with 20 episodes per season will have Season 3 episode 10 listed as 50. The field is mostly used with cartoons and anime series as they may have ambiguous seasons making it easier to use this field.
    @Element(required = false, name="absolute_number")
    public int absoluteNumber;

    // An unsigned integer indicating the season number this episode comes after. This field is only available for special episodes. Can be null.
    @Element(required = false, name="airsafter_season")
    public int airsAfterSeason;

    // An unsigned integer indicating the episode number this special episode airs before. Must be used in conjunction with airsbefore_season, do not with airsafter_season. This field is only available for special episodes. Can be null.
    @Element(required = false, name="airsbefore_episode")
    public int airsBeforeEpisode;

    // An unsigned integer indicating the season number this special episode airs before. Should be used in conjunction with airsbefore_episode for exact placement. This field is only available for special episodes. Can be null.
    @Element(required = false, name="airsbefore_season")
    public int airsBeforeSeason;

    // A string which should be appended to <mirrorpath>/banners/ to determine the actual location of the artwork. Returns the location of the episode image. Can be null.
    @Element(required = false)
    public String filename;

    // Unix time stamp indicating the last time any changes were made to the episode. Can be null.
    @Element(required = false, name="lastupdated")
    public long lastUpdated;

    // An unsigned integer assigned by our site to the season. Cannot be null.
    @Element(name="seasonid")
    public int seasonId;

    // An unsigned integer assigned by our site to the series. It does not change and will always represent the same series. Cannot be null.
    @Element(name="seriesid")
    public int seriesId;

    // A string containing the time the episode image was added to our site in the format "YYYY-MM-DD HH:MM:SS" based on a 24 hour clock. Can be null.
    @Element(required = false, name="thumb_added")
    public String thumbAdded;

    // An unsigned integer that represents the height of the episode image in pixels. Can be null
    @Element(required = false, name="thumb_height")
    public int thumbHeight;

    // An unsigned integer that represents the width of the episode image in pixels. Can be null
    @Element(required = false, name="thumb_width")
    public int thumbWidth;
}
