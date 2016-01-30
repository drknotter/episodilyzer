package com.drknotter.episodilyzer.utils;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.BannerList;
import com.drknotter.episodilyzer.server.model.FullSeries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.SimpleXMLConverter;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

/**
 * Created by plunkett on 1/24/16.
 */
public class SeriesUtils {
    private static HandlerThread handlerThread;
    private static Handler handler;

    public interface OnSavedSeriesFetchedListener {
        void onSavedSeriesFetched(List<FullSeries> savedSeries);
    }

    private SeriesUtils() {
    }

    public static synchronized void saveSeries(int seriesId) {
        startThreadIfNeeded();
        Message msg = handler.obtainMessage(SeriesUtilsHandler.WHAT_SAVE, seriesId);
        handler.sendMessage(msg);
    }

    public static synchronized void deleteSeries(int seriesId) {
        startThreadIfNeeded();
        Message msg = handler.obtainMessage(SeriesUtilsHandler.WHAT_DELETE, seriesId);
        handler.sendMessage(msg);
    }

    public static synchronized void fetchSavedSeries(OnSavedSeriesFetchedListener listener) {
        startThreadIfNeeded();
        Message msg = handler.obtainMessage(SeriesUtilsHandler.WHAT_FETCH_SAVED, listener);
        handler.sendMessage(msg);
    }

    public static synchronized boolean isSeriesSaved(int seriesId) {
        return new File(Episodilyzer.getInstance().getRootSeriesDir(),
                Integer.toString(seriesId)).exists();
    }

    private static void startThreadIfNeeded() {
        if (handlerThread == null) {
            handlerThread = new HandlerThread("SeriesUtils");
            handlerThread.start();
            handler = new SeriesUtilsHandler(handlerThread.getLooper());
        }
    }

    private static class SeriesUtilsHandler extends android.os.Handler {
        public static final int WHAT_SAVE = 0;
        public static final int WHAT_DELETE = 1;
        public static final int WHAT_FETCH_SAVED = 2;

        public SeriesUtilsHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(final Message msg) {
            switch (msg.what) {
                case WHAT_SAVE: {
                    int seriesId = (int) msg.obj;

                    Log.v("FindMe", "Saving series " + seriesId);
                    Response response = new RestAdapter.Builder()
                            .setEndpoint(TheTVDBService.BASE_URL)
                            .build()
                            .create(TheTVDBService.class)
                            .getSeriesInfo(
                                    Episodilyzer.getInstance().getString(R.string.api_key),
                                    seriesId);

                    try {
                        File seriesDir = new File(Episodilyzer.getInstance().getRootSeriesDir(),
                                Integer.toString(seriesId));
                        seriesDir.mkdirs();

                        InputStream zipfileInputStream = response.getBody().in();
                        File zipFile = downloadZipFile(zipfileInputStream, seriesDir, seriesId);
                        zipfileInputStream.close();

                        unzip(zipFile, seriesDir);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case WHAT_DELETE: {
                    int seriesId = (int) msg.obj;

                    Log.v("FindMe", "Deleting series " + seriesId);
                    File seriesDir = new File(Episodilyzer.getInstance().getRootSeriesDir(),
                            Integer.toString(seriesId));
                    nukeDirectory(seriesDir);
                    Log.v("FindMe", "Deleted series " + seriesId);

                    break;
                }

                case WHAT_FETCH_SAVED: {
                    SimpleXMLConverter converter = new SimpleXMLConverter();
                    final List<FullSeries> savedSeries = new ArrayList<>();

                    File rootSeriesDir = Episodilyzer.getInstance().getRootSeriesDir();
                    for (File seriesDir : rootSeriesDir.listFiles()) {
                        try {
                            File fullSeriesFile = new File(seriesDir, "en.xml");
                            TypedInput input = new TypedFile("text/xml", fullSeriesFile);
                            Log.v("FindMe", "deserializing series " + seriesDir.getName());
                            long now = System.nanoTime();
                            FullSeries series = (FullSeries) converter.fromBody(input, FullSeries.class);
                            Log.v("FindMe", "deserialized series, took " + (System.nanoTime() - now) / 1000000 + "ms");

                            File bannersFile = new File(seriesDir, "banners.xml");
                            input = new TypedFile("text/xml", bannersFile);
                            Log.v("FindMe", "deserializing banners");
                            now = System.nanoTime();
                            BannerList bannerList = (BannerList) converter.fromBody(input, BannerList.class);
                            Log.v("FindMe", "deserialized banners, took " + (System.nanoTime() - now) / 1000000 + "ms");
                            series.banners = bannerList.banners;

                            savedSeries.add(series);
                        } catch (ConversionException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.v("FindMe", "fetched " + savedSeries.size() + " series");

                    new Handler(Looper.getMainLooper()).post(
                            new SavedSeriesFetchedCaller((OnSavedSeriesFetchedListener) msg.obj, savedSeries));

                    break;
                }
            }
        }
    }

    private static File downloadZipFile(InputStream in, File seriesDir, int seriesId) throws IOException {
        File zipFile = new File(seriesDir, Integer.toString(seriesId) + ".zip");

        OutputStream zipFileOutputStream = new FileOutputStream(zipFile);
        byte[] buffer = new byte[1024];

        int count;
        while ((count = in.read(buffer)) != -1) {
            zipFileOutputStream.write(buffer, 0, count);
        }

        zipFileOutputStream.flush();
        zipFileOutputStream.close();

        return zipFile;
    }

    private static void unzip(File zipFile, File dir) throws IOException {
        ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
        ZipEntry zipEntry;
        while ((zipEntry = zipInputStream.getNextEntry()) != null) {
            if (zipEntry.isDirectory()) {
                new File(dir, zipEntry.getName()).mkdir();
            } else {
                byte[] buffer = new byte[1024];
                FileOutputStream fileOutputStream = new FileOutputStream(
                        new File(dir, zipEntry.getName()));

                int count;
                while ((count = zipInputStream.read(buffer)) != -1) {
                    fileOutputStream.write(buffer, 0, count);
                }

                zipInputStream.closeEntry();
                fileOutputStream.close();
            }

        }
        zipInputStream.close();
    }

    private static void nukeDirectory(File file)
    {
        if(file.isDirectory()) {
            for(File child : file.listFiles()) {
                nukeDirectory(child);
            }
        }
        file.delete();
    }

    private static class SavedSeriesFetchedCaller implements Runnable {
        private OnSavedSeriesFetchedListener listener;
        private List<FullSeries> savedSeries;

        SavedSeriesFetchedCaller(OnSavedSeriesFetchedListener listener, List<FullSeries> savedSeries) {
            this.listener = listener;
            this.savedSeries = savedSeries;
        }

        @Override
        public void run() {
            listener.onSavedSeriesFetched(savedSeries);
        }
    }
}
