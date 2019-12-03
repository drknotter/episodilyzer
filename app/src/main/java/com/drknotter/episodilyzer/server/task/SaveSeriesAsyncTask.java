package com.drknotter.episodilyzer.server.task;

import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.event.SeriesSaveFailEvent;
import com.drknotter.episodilyzer.event.SeriesSaveStartEvent;
import com.drknotter.episodilyzer.event.SeriesSaveSuccessEvent;
import com.drknotter.episodilyzer.model.Banner;
import com.drknotter.episodilyzer.model.Episode;
import com.drknotter.episodilyzer.model.Series;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.ActorList;
import com.drknotter.episodilyzer.server.model.BannerList;
import com.drknotter.episodilyzer.server.model.EpisodeList;
import com.drknotter.episodilyzer.server.model.Links;
import com.drknotter.episodilyzer.server.model.SeriesResponse;
import com.drknotter.episodilyzer.server.model.SeriesSearchResult;
import com.drknotter.episodilyzer.utils.RequestUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Response;

public class SaveSeriesAsyncTask extends AutheticatedRequestTask<com.drknotter.episodilyzer.model.Series> {
    private static final String TAG = "SaveSeriesAsyncTask";
    private SeriesSearchResult searchResult;

    public SaveSeriesAsyncTask(SeriesSearchResult searchResult) {
        super(new TaskCallback<com.drknotter.episodilyzer.model.Series>() {
            @Override
            public void onSuccess(com.drknotter.episodilyzer.model.Series series) {
                EventBus.getDefault().post(new SeriesSaveSuccessEvent(series));
            }

            @Override
            public void onError(String errorMessage) {
                EventBus.getDefault().post(new SeriesSaveFailEvent(errorMessage));
            }
        });
        this.searchResult = searchResult;
    }

    @Override
    protected void onPreExecute() {
        EventBus.getDefault().post(new SeriesSaveStartEvent(searchResult));
    }

    @Override
    protected Void doInBackground(Void... params) {
        saveSeries(searchResult.id);
        return null;
    }

    @Override
    protected void onCancelled() {
        EventBus.getDefault().post(new SeriesSaveFailEvent(
                Episodilyzer.getInstance().getString(
                        R.string.snack_series_save_failed, searchResult.seriesName)));
    }

    private void saveSeries(final int seriesId) {
        // First get the series information.
        Response<SeriesResponse> seriesResponse
                = fetchAuthenticatedResponse(
                        new Callable<Response<SeriesResponse>>() {
                            @Override
                            public Response<SeriesResponse> call() {
                                Response<SeriesResponse> response
                                        = null;
                                try {
                                    response = getService().getSeries(
                                            RequestUtils.getBearerString(), seriesId).execute();
                                } catch (IOException e) {
                                    Log.w(TAG, "Unable to fetch series info!");
                                }
                                return response;
                            }
                        });
        if (seriesResponse == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_network_error, searchResult.seriesName));
            return;
        }
        if (!seriesResponse.isSuccessful()
                || seriesResponse.body() == null
                || seriesResponse.body().data == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_with_message,
                    searchResult.seriesName, seriesResponse.message()));
            return;
        }

        // Then the episodes.
        Response<EpisodeList> episodeListResponse = fetchAuthenticatedResponse(
                new EpisodeListFetcher(getService(), searchResult.id, null));
        if (episodeListResponse == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_network_error, searchResult.seriesName));
            return;
        }
        if (!episodeListResponse.isSuccessful()
                || episodeListResponse.body() == null
                || episodeListResponse.body().data == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_with_message,
                    searchResult.seriesName, seriesResponse.message()));
            return;
        }

        List<com.drknotter.episodilyzer.server.model.Episode> episodes
                = new ArrayList<>(episodeListResponse.body().data);
        if (episodeListResponse.body().links != null) {
            Links l = episodeListResponse.body().links;
            for (int i = l.next; i < l.last; i++) {
                episodeListResponse = fetchAuthenticatedResponse(
                        new EpisodeListFetcher(getService(), searchResult.id, i));
                if (episodeListResponse == null) {
                    setErrorMessage(Episodilyzer.getInstance().getString(
                            R.string.snack_series_save_failed_network_error,
                            searchResult.seriesName));
                    return;
                }
                if (!episodeListResponse.isSuccessful()
                        || episodeListResponse.body() == null
                        || episodeListResponse.body().data == null) {
                    setErrorMessage(Episodilyzer.getInstance().getString(
                            R.string.snack_series_save_failed_with_message,
                            searchResult.seriesName, episodeListResponse.message()));
                    return;
                }
                episodes.addAll(episodeListResponse.body().data);
            }
        }

        // Then the actors.
        Response<ActorList> actorListResponse = fetchAuthenticatedResponse(
                new Callable<Response<ActorList>>() {
                    @Override
                    public Response<ActorList> call() {
                        Response<ActorList> response = null;
                        try {
                            response = getService().getSeriesActors(
                                    RequestUtils.getBearerString(), seriesId).execute();
                        } catch (IOException e) {
                            Log.w(TAG, "Unable to fetch actors info!");
                        }
                        return response;
                    }
                });
        if (actorListResponse == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_network_error, searchResult.seriesName));
            return;
        }
        if (!actorListResponse.isSuccessful()
                || actorListResponse.body() == null
                || actorListResponse.body().data == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_with_message,
                    searchResult.seriesName, actorListResponse.message()));
            return;
        }

        // Then the banners.
        Response<BannerList> bannerListResponse = fetchAuthenticatedResponse(
                new Callable<Response<BannerList>>() {
                    @Override
                    public Response<BannerList> call() {
                        Response<BannerList> response = null;
                        try {
                            response = getService().getBanners(
                                    RequestUtils.getBearerString(), seriesId,
                                    "fanart", null).execute();
                        } catch (IOException e) {
                            Log.w(TAG, "Unable to fetch banner info!");
                        }
                        return response;
                    }
                });
        if (bannerListResponse == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_network_error, searchResult.seriesName));
            return;
        }
        if (!bannerListResponse.isSuccessful()
                || bannerListResponse.body() == null
                || bannerListResponse.body().data == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_with_message,
                    searchResult.seriesName, bannerListResponse.message()));
            return;
        }

        List<com.drknotter.episodilyzer.server.model.Banner> banners
                = new ArrayList<>(bannerListResponse.body().data);
        bannerListResponse = fetchAuthenticatedResponse(
                new Callable<Response<BannerList>>() {
                    @Override
                    public Response<BannerList> call() {
                        Response<BannerList> response = null;
                        try {
                            response = getService().getBanners(
                                    RequestUtils.getBearerString(), seriesId,
                                    "series", "graphical").execute();
                        } catch (IOException e) {
                            Log.w(TAG, "Unable to fetch banner info!");
                        }
                        return response;
                    }
                });
        if (bannerListResponse == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_network_error, searchResult.seriesName));
            return;
        }
        if (!bannerListResponse.isSuccessful()
                || bannerListResponse.body() == null
                || bannerListResponse.body().data == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(
                    R.string.snack_series_save_failed_with_message,
                    searchResult.seriesName, bannerListResponse.message()));
            return;
        }
        banners.addAll(bannerListResponse.body().data);

        // Save the series info.
        Series series = new Series(seriesResponse.body().data, actorListResponse.body().data);
        series.save();

        // Then save the episode info.
        ActiveAndroid.beginTransaction();
        try {
            for (com.drknotter.episodilyzer.server.model.Episode episode : episodes) {
                new Episode(episode, series, 31 * episode.id + series.id).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        ActiveAndroid.beginTransaction();
        try {
            for (com.drknotter.episodilyzer.server.model.Banner banner : banners) {
                new Banner(banner, series).save();
            }
            ActiveAndroid.setTransactionSuccessful();
        } finally {
            ActiveAndroid.endTransaction();
        }

        setResult(series);
    }

    private static class EpisodeListFetcher implements Callable<Response<EpisodeList>> {
        private TheTVDBService service;
        private int seriesId;
        private Integer page;

        public EpisodeListFetcher(TheTVDBService service, int seriesId, Integer page) {
            this.service = service;
            this.seriesId = seriesId;
            this.page = page;
        }

        @Override
        public Response<EpisodeList> call() {
            Response<EpisodeList> response
                    = null;
            try {
                response = service.getEpisodes(
                        RequestUtils.getBearerString(), seriesId, page).execute();
            } catch (IOException e) {
                Log.w(TAG, "Unable to fetch episode info!");
            }
            return response;
        }
    }
}
