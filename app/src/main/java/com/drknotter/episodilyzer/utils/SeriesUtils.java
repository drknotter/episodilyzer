package com.drknotter.episodilyzer.utils;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.model.Series;

import java.util.List;

public class SeriesUtils {
    private SeriesUtils() {
    }

    public static List<Series> allSeries() {
        return new Select().from(Series.class)
                .orderBy("lastAccessed DESC, seriesName")
                .execute();
    }

    public static boolean isSeriesSaved(int seriesId) {
        return new Select().from(Series.class)
                .where("series_id = ?", seriesId)
                .exists();
    }

    public static void deleteSeries(int seriesId) {
        new Delete().from(Series.class)
                .where("series_id = ?", seriesId)
                .execute();
    }
}
