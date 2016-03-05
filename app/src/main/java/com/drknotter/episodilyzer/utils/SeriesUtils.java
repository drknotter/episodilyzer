package com.drknotter.episodilyzer.utils;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.model.SaveSeriesInfo;
import com.drknotter.episodilyzer.server.task.SaveSeriesAsyncTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeriesUtils {
    private static Map<Integer, SaveSeriesAsyncTask> saveSeriesTaskMap = new HashMap<>();
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

    public static synchronized void saveSeries(final SaveSeriesInfo seriesInfo) {
        if (seriesInfo == null) {
            return;
        }

        if (!saveSeriesTaskMap.containsKey(seriesInfo.seriesId)) {
            SaveSeriesAsyncTask task = new SaveSeriesAsyncTask(seriesInfo) {
                @Override
                protected void onPostExecute(Void result) {
                    super.onPostExecute(result);
                    saveSeriesTaskMap.remove(seriesInfo.seriesId);
                }

                @Override
                protected void onCancelled() {
                    super.onCancelled();
                }
            };
            task.execute();
        }
    }

}
