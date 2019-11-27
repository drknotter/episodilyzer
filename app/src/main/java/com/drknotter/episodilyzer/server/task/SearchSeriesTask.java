package com.drknotter.episodilyzer.server.task;

import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.SearchResult;
import com.drknotter.episodilyzer.utils.RequestUtils;

import java.io.IOException;

import retrofit2.Response;

public class SearchSeriesTask extends AutheticatedRequestTask<SearchResult> {
    private final String name;

    public SearchSeriesTask(String name, TaskCallback<SearchResult> callback) {
        super(callback);
        this.name = name;
    }

    @Override
    protected SearchResult doInBackground(Void... voids) {
        Response<SearchResult> searchResponse = fetchAuthenticatedResponse();

        if (searchResponse != null && searchResponse.isSuccessful()
                && searchResponse.body() != null) {
            return searchResponse.body();
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

    @Override
    Response<SearchResult> fetchResponse() {
        Response<SearchResult> searchResponse = null;
        try {
            searchResponse = getService().searchShows(
                    RequestUtils.getBearerString(getAuthToken()), name).execute();
        } catch (IOException ignored) {}

        if (searchResponse == null) {
            setErrorMessage(Episodilyzer.getInstance().getString(R.string.network_error));
        }
        return searchResponse;
    }

}
