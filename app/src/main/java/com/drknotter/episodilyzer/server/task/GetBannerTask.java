package com.drknotter.episodilyzer.server.task;

import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.BannerList;
import com.drknotter.episodilyzer.utils.RequestUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

import retrofit2.Response;

public class GetBannerTask extends AutheticatedRequestTask<BannerList> {
    private final int seriesId;
    private final String type;
    private final String subType;

    public GetBannerTask(int seriesId, String type, String subType, TaskCallback<BannerList> callback) {
        super(callback);
        this.seriesId = seriesId;
        this.type = type;
        this.subType = subType;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Response<BannerList> bannerResponse = fetchAuthenticatedResponse(
                new Callable<Response<BannerList>>() {
                    @Override
                    public Response<BannerList> call() {
                        Response<BannerList> bannerListResponse = null;
                        try {
                            bannerListResponse = getService().getBanners(
                                    RequestUtils.getBearerString(),
                                    seriesId, type, subType).execute();
                        } catch (IOException ignored) {}

                        if (bannerListResponse == null) {
                            setErrorMessage(
                                    Episodilyzer.getInstance().getString(R.string.network_error));
                        }
                        return bannerListResponse;
                    }
                }
        );

        if (bannerResponse != null && bannerResponse.isSuccessful()
                && bannerResponse.body() != null) {
            setResult(bannerResponse.body());
            return null;
        } else {
            setErrorMessage(Episodilyzer.getInstance().getString(R.string.search_failed));
            if (bannerResponse == null || bannerResponse.body() == null) {
                setErrorMessage(Episodilyzer.getInstance().getString(R.string.no_response));
            } else if (!bannerResponse.isSuccessful()) {
                setErrorMessage(Episodilyzer.getInstance().getString(
                        R.string.search_failed_with_message, bannerResponse.message()));
            }
        }

        return null;
    }
}
