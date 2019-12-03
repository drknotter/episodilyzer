package com.drknotter.episodilyzer.model;

import android.net.Uri;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.drknotter.episodilyzer.server.TheTVDBService;

/**
 * Created by plunkett on 1/29/16.
 */
public class Banner extends Model {
    public static final String TYPE_SERIES = "series";
    public static final String TYPE_FANART = "fanart";

    public Banner() {}

    public Banner(com.drknotter.episodilyzer.server.model.Banner banner, Series series) {
        super();
        this.series = series;
        path = banner.fileName;
        thumbnailPath = banner.thumbnail;
//        vignettePath = banner.vignettePath;
        type = banner.keyType;
        type2 = banner.subKey;
//        season = banner.season;
        rating = banner.ratingsInfo != null ? banner.ratingsInfo.average : 0;
        ratingCount = banner.ratingsInfo != null ? banner.ratingsInfo.count : 0;
//        seriesName = banner.seriesName;
//        colors = banner.colors;
    }

    @Column(name="series", onUpdate = Column.ForeignKeyAction.CASCADE, onDelete = Column.ForeignKeyAction.CASCADE)
    public Series series;

    // Can be appended to <mirrorpath>/banners/ to determine the actual location of the artwork.
    @Column(name="path", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public String path;

    // Used exactly the same way as BannerPath, only shows if BannerType is fanart.
    @Column(name="thumbnail")
    public String thumbnailPath;
    @Column(name="vignettePath")
    public String vignettePath;

    // This can be poster, fanart, series or season.
    @Column(name="type", index = true)
    public String type;

    // For series banners it can be text, graphical, or blank. For season banners it can be season or seasonwide. For fanart it can be 1280x720 or 1920x1080. For poster it will always be 680x1000. Blank banners will leave the title and show logo off the banner. Text banners will show the series name as plain text in an Arial font. Graphical banners will show the series name in the show's official font or will display the actual logo for the show. Season banners are the standard DVD cover format while wide season banners will be the same dimensions as the series banners.
    @Column(name="subKey")
    public String type2;

    // If the banner is for a specific season, that season number will be listed here.
    @Column(name="season")
    public int season;

    // Returns either null or a decimal with four decimal places. The rating the banner currently has on the site.
    @Column(name="rating", index = true)
    public float rating;

    // Always returns an unsigned integer. Number of people who have rated the image.
    @Column(name="ratingCount")
    public int ratingCount;

    // This can be true or false. Only shows if BannerType is fanart. Indicates if the seriesname is included in the image or not.
    @Column(name="seriesName")
    public boolean seriesName;

    // Returns either null or three RGB colors in decimal format and pipe delimited. These are colors the artist picked that go well with the image. In order they are Light Accent Color, Dark Accent Color and Neutral Midtone Color. It's meant to be used if you want to write something over the image, it gives you a good idea of what colors may work and show up well. Only shows if BannerType is fanart.
    @Column(name="colors")
    public String colors;

    public Uri uri() {
        return Uri.parse(TheTVDBService.WEB_URL)
                .buildUpon()
                .appendPath("banners")
                .appendPath(path)
                .build();
    }
}
