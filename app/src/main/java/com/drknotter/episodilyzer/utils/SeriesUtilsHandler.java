package com.drknotter.episodilyzer.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.BannerList;
import com.drknotter.episodilyzer.server.model.BaseBanner;
import com.drknotter.episodilyzer.server.model.BaseEpisode;
import com.drknotter.episodilyzer.server.model.FullSeries;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.SimpleXMLConverter;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by plunkett on 1/30/16.
 */
public class SeriesUtilsHandler extends Handler {
    public static final int WHAT_SAVE = 0;
    public static final int WHAT_SAVE_BATCH = 1;
    public static final int WHAT_DELETE = 2;
    public static final int WHAT_DELETE_BATCH = 3;
    public static final int WHAT_FETCH_SAVED = 4;

    private static Handler uiHandler = new Handler(Looper.getMainLooper());

    public SeriesUtilsHandler(Looper looper) {
        super(looper);
    }

    @Override
    public void handleMessage(final Message msg) {
        switch (msg.what) {
            case WHAT_SAVE: {
                int seriesId = (int) msg.obj;
                saveSeries(seriesId, true);
                break;
            }

            case WHAT_SAVE_BATCH: {
                @SuppressWarnings("unchecked")
                List<Integer> seriesIds = (List<Integer>) msg.obj;
                for (Integer seriesId : seriesIds) {
                    saveSeries(seriesId, true);
                }
                break;
            }

            case WHAT_DELETE: {
                int seriesId = (int) msg.obj;
                List<Series> deletedSeries = new Delete().from(Series.class)
                        .where("series_id = ?", seriesId)
                        .execute();
                SeriesUtils.notifyDeleted(deletedSeries);
                break;
            }

            case WHAT_FETCH_SAVED: {
                SeriesUtils.OnSavedSeriesFetchedListener listener = (SeriesUtils.OnSavedSeriesFetchedListener) msg.obj;
                List<Series> savedSeries = new Select().from(Series.class)
                        .orderBy("lastAccessed DESC, seriesName")
                        .execute();
                uiHandler.post(new FetchedRunnable(listener, savedSeries));
                break;
            }
        }
    }

    private void saveSeries(int seriesId, boolean notify) {
        Response response = new RestAdapter.Builder()
                .setEndpoint(TheTVDBService.BASE_URL)
                .build()
                .create(TheTVDBService.class)
                .getSeriesInfo(
                        Episodilyzer.getInstance().getString(R.string.api_key),
                        seriesId);

        File seriesDir = new File(Episodilyzer.getInstance().getCacheDir(),
                Integer.toString(seriesId));
        seriesDir.mkdirs();
        try {
            InputStream zipfileInputStream = response.getBody().in();
            File zipFile = FilesystemUtils.downloadZipFile(zipfileInputStream, seriesDir, seriesId);
            zipfileInputStream.close();

            FilesystemUtils.unzip(zipFile, seriesDir);

            SimpleXMLConverter converter = new SimpleXMLConverter();

            // Deserialize the full series information.
            File fullSeriesFile = new File(seriesDir, "en.xml");
            TypedInput input = new TypedFile("text/xml", fullSeriesFile);
            FullSeries fullSeries = (FullSeries) converter.fromBody(input, FullSeries.class);

            boolean exists = new Select()
                    .from(Series.class)
                    .where("series_id = ?", fullSeries.series.id)
                    .exists();

            Series series = new Series(fullSeries);
            series.save();

            ActiveAndroid.beginTransaction();
            try {
                for (BaseEpisode baseEpisode : fullSeries.episodes) {
                    new Episode(baseEpisode, series).save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

            File bannersFile = new File(seriesDir, "banners.xml");
            input = new TypedFile("text/xml", bannersFile);
            BannerList bannerList = (BannerList) converter.fromBody(input, BannerList.class);

            ActiveAndroid.beginTransaction();
            try {
                for (BaseBanner baseBanner : bannerList.banners) {
                    new Banner(baseBanner, series).save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

            if (notify) {
                List<Series> seriesList = new ArrayList<>();
                seriesList.add(series);
                uiHandler.post(new NotifyRunnable(
                        exists ? NotifyRunnable.Type.UPDATE : NotifyRunnable.Type.INSERT,
                        seriesList));
            }

        } catch (IOException |ConversionException e) {
            e.printStackTrace();

        } finally {
            // Clean up after ourselves.
            FilesystemUtils.nukeDirectory(seriesDir);
        }
    }

    private static class NotifyRunnable implements Runnable {
        enum Type {
            INSERT,
            DELETE,
            UPDATE,
        }
        private Type type;
        private List<Series> seriesList;

        public NotifyRunnable(Type type, List<Series> seriesList) {
            this.type = type;
            this.seriesList = seriesList;
        }

        @Override
        public void run() {
            switch (type) {
                case INSERT:
                    SeriesUtils.notifyInserted(seriesList);
                    break;

                case DELETE:
                    SeriesUtils.notifyDeleted(seriesList);
                    break;

                case UPDATE:
                    SeriesUtils.notifyUpdated(seriesList);
                    break;
            }
        }
    }

    private static class FetchedRunnable implements Runnable {
        private WeakReference<SeriesUtils.OnSavedSeriesFetchedListener> listenerRef;
        private List<Series> seriesList;

        public FetchedRunnable(SeriesUtils.OnSavedSeriesFetchedListener listener, List<Series> seriesList) {
            listenerRef = new WeakReference<>(listener);
            this.seriesList = seriesList;
        }

        @Override
        public void run() {
            SeriesUtils.OnSavedSeriesFetchedListener listener = listenerRef.get();
            if (listener != null) {
                listener.onSavedSeriesFetched(seriesList);
            }
        }

    }
}
