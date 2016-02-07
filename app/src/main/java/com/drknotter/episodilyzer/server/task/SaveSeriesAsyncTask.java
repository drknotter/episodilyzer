package com.drknotter.episodilyzer.server.task;

import android.os.AsyncTask;

import com.activeandroid.ActiveAndroid;
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
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import retrofit.converter.SimpleXMLConverter;
import retrofit.mime.TypedFile;
import retrofit.mime.TypedInput;

public class SaveSeriesAsyncTask extends AsyncTask<Integer, Void, List<Series>> {
    @Override
    protected List<Series> doInBackground(Integer[] seriesIds) {
        List<Series> result = new ArrayList<>();
        for (Integer seriesId : seriesIds) {
            result.add(saveSeries(seriesId));
        }
        return result;
    }

    private Series saveSeries(int seriesId) {
        Series series = null;

        Response response = new RestAdapter.Builder()
                .setEndpoint(TheTVDBService.BASE_URL)
                .build()
                .create(TheTVDBService.class)
                .getSeriesInfo(
                        Episodilyzer.getInstance().getString(R.string.api_key),
                        seriesId);

        File seriesDir = new File(Episodilyzer.getInstance().getCacheDir(),
                Integer.toString(seriesId));
        //noinspection ResultOfMethodCallIgnored
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

            series = new Series(fullSeries);
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

        } catch (IOException |ConversionException e) {
            e.printStackTrace();

        } finally {
            // Clean up after ourselves.
            FilesystemUtils.nukeDirectory(seriesDir);
        }

        return series;
    }
}
