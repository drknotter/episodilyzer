package com.drknotter.episodilyzer.server.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "Banner", strict = false)
public class Banner {
    // Can be appended to <mirrorpath>/banners/ to determine the actual location of the artwork.
    @Element(name="BannerPath")
    public String bannerPath;

    // Used exactly the same way as BannerPath, only shows if BannerType is fanart.
    @Element(name="ThumbnailPath", required=false)
    public String thumbnailPath;
    @Element(name="VignettePath", required=false)
    public String vignettePath;

    // This can be poster, fanart, series or season.
    @Element(name="BannerType", required=false)
    public String bannerType;

    // For series banners it can be text, graphical, or blank. For season banners it can be season or seasonwide. For fanart it can be 1280x720 or 1920x1080. For poster it will always be 680x1000. Blank banners will leave the title and show logo off the banner. Text banners will show the series name as plain text in an Arial font. Graphical banners will show the series name in the show's official font or will display the actual logo for the show. Season banners are the standard DVD cover format while wide season banners will be the same dimensions as the series banners.
    @Element(name="BannerType2", required=false)
    public String bannerType2;

    // Some banners list the series name in a foreign language. The language abbreviation will be listed here.
    @Element(name="Language", required=false)
    public String language;

    // If the banner is for a specific season, that season number will be listed here.
    @Element(name="Season", required=false)
    public int season;

    // Returns either null or a decimal with four decimal places. The rating the banner currently has on the site.
    @Element(name="Rating", required=false)
    public float rating;

    // Always returns an unsigned integer. Number of people who have rated the image.
    @Element(name="RatingCount", required=false)
    public int ratingCount;

    // This can be true or false. Only shows if BannerType is fanart. Indicates if the seriesname is included in the image or not.
    @Element(name="SeriesName", required=false)
    public boolean seriesName;

    // Returns either null or three RGB colors in decimal format and pipe delimited. These are colors the artist picked that go well with the image. In order they are Light Accent Color, Dark Accent Color and Neutral Midtone Color. It's meant to be used if you want to write something over the image, it gives you a good idea of what colors may work and show up well. Only shows if BannerType is fanart.
    @Element(name="Colors", required=false)
    public String colors;
}
