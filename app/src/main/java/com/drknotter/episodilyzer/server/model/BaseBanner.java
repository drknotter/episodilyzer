package com.drknotter.episodilyzer.server.model;

public class BaseBanner {
    // Can be appended to <mirrorpath>/banners/ to determine the actual location of the artwork.
    public String fileName;

    public int id;

    // This can be poster, fanart, series or season.
    public String keyType;

    public RatingsInfo ratingsInfo;

    // For series banners it can be text, graphical, or blank. For season banners it can be season or seasonwide. For fanart it can be 1280x720 or 1920x1080. For poster it will always be 680x1000. Blank banners will leave the title and show logo off the banner. Text banners will show the series name as plain text in an Arial font. Graphical banners will show the series name in the show's official font or will display the actual logo for the show. Season banners are the standard DVD cover format while wide season banners will be the same dimensions as the series banners.
    public String subKey;

    // Used exactly the same way as BannerPath, only shows if BannerType is fanart.
    public String thumbnail;
}
