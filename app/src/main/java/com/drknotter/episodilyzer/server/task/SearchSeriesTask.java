package com.drknotter.episodilyzer.server.task;

import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.SeriesSearchResultList;
import com.drknotter.episodilyzer.utils.RequestUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

import retrofit2.Response;

public class SearchSeriesTask extends AutheticatedRequestTask<SeriesSearchResultList> {
    private final String name;

    public SearchSeriesTask(String name, TaskCallback<SeriesSearchResultList> callback) {
        super(callback);
        this.name = name;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Response<SeriesSearchResultList> searchResponse = fetchAuthenticatedResponse(
                new Callable<Response<SeriesSearchResultList>>() {
                    @Override
                    public Response<SeriesSearchResultList> call(){
                        Response<SeriesSearchResultList> searchResponse = null;
                        try {
                            searchResponse = getService().searchShows(
                                    RequestUtils.getBearerString(), name).execute();
                        } catch (IOException ignored) {}

                        if (searchResponse == null) {
                            setErrorMessage(
                                    Episodilyzer.getInstance().getString(R.string.network_error));
                        }
                        return searchResponse;
                    }
                });

        if (searchResponse != null && searchResponse.isSuccessful()
                && searchResponse.body() != null) {
            setResult(searchResponse.body());
            return null;
        } else {
            setErrorMessage(Episodilyzer.getInstance().getString(R.string.search_failed));
            if (searchResponse == null || searchResponse.body() == null) {
                setErrorMessage(Episodilyzer.getInstance().getString(R.string.no_response));
            } else if (!searchResponse.isSuccessful()) {
                setErrorMessage(Episodilyzer.getInstance().getString(
                        R.string.search_failed_with_message, searchResponse.message()));
            }
        }

        return null;
    }
}
