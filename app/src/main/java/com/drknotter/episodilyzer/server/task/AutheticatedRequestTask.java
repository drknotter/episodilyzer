package com.drknotter.episodilyzer.server.task;

import android.os.AsyncTask;

import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.model.AuthTokenRequest;
import com.drknotter.episodilyzer.model.AuthTokenResponse;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.server.model.SearchResult;
import com.drknotter.episodilyzer.utils.PreferenceUtils;

import java.io.IOException;

import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AutheticatedRequestTask<T> extends AsyncTask<Void, Void, T> {
    private final TheTVDBService service;
    private final TaskCallback<T> callback;

    private String token;
    private String errorMessage;

    AutheticatedRequestTask(TaskCallback<T> callback) {
        this.service = new Retrofit.Builder()
                .baseUrl(TheTVDBService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheTVDBService.class);
        this.callback = callback;
    }

    @Override
    protected void onPostExecute(T result) {
        if (getErrorMessage() != null) {
            callback.onError(getErrorMessage());
        } else if (result == null) {
            callback.onError(Episodilyzer.getInstance().getString(R.string.no_response));
        } else {
            callback.onSuccess(result);
        }
    }


    abstract Response<T> fetchResponse();

    final Response<T> fetchAuthenticatedResponse() {
        if (getAuthToken() == null && !fetchAuthToken()) {
            return null;
        }

        Response<T> response = fetchResponse();
        if (response == null) {
            return null;
        }

        // Refetch the auth token, if it's expired.
        if (response.code() == 401) {
            if (!fetchAuthToken()) {
                return null;
            }
            response = fetchResponse();
        }

        return response;
    }

    final boolean fetchAuthToken() {
        Response<AuthTokenResponse> tokenResponse;
        try {
            tokenResponse = service.getAuthToken(new AuthTokenRequest(
                    Episodilyzer.getInstance().getString(R.string.api_key)))
                    .execute();
        } catch (IOException e) {
            setErrorMessage(Episodilyzer.getInstance().getString(R.string.network_error));
            return false;
        }
        if (tokenResponse.isSuccessful() && tokenResponse.body() != null
                && tokenResponse.body().token != null) {
            token = tokenResponse.body().token;
            PreferenceUtils.setAuthToken(token);
        } else {
            errorMessage = Episodilyzer.getInstance().getString(R.string.auth_failed);
            if (tokenResponse.body() == null) {
                errorMessage = Episodilyzer.getInstance().getString(R.string.no_response);
            } else if (!tokenResponse.isSuccessful()) {
                errorMessage = Episodilyzer.getInstance().getString(
                        R.string.auth_failed_with_message, tokenResponse.message());
            }
            return false;
        }

        return true;
    }

    final TheTVDBService getService() {
        return service;
    }

    final String getAuthToken() {
        return token;
    }

    final void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    final String getErrorMessage() {
        return errorMessage;
    }
}
