package com.drknotter.episodilyzer.server.task;

import android.os.AsyncTask;
import android.util.Log;

import com.drknotter.episodilyzer.Episodilyzer;
import com.drknotter.episodilyzer.R;
import com.drknotter.episodilyzer.server.model.AuthTokenRequest;
import com.drknotter.episodilyzer.server.model.AuthTokenResponse;
import com.drknotter.episodilyzer.server.TheTVDBService;
import com.drknotter.episodilyzer.utils.PreferenceUtils;

import java.io.IOException;
import java.util.concurrent.Callable;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class AutheticatedRequestTask<T> extends AsyncTask<Void, Void, Void> {
    private final TheTVDBService service;
    private final TaskCallback<T> callback;

    private String errorMessage;
    private T result;

    AutheticatedRequestTask(TaskCallback<T> callback) {
        this.service = new Retrofit.Builder()
                .baseUrl(TheTVDBService.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(TheTVDBService.class);
        this.callback = callback;
    }

    @Override
    protected void onPostExecute(Void ignored) {
        if (errorMessage != null) {
            callback.onError(errorMessage);
        } else if (result == null) {
            callback.onError(Episodilyzer.getInstance().getString(R.string.no_response));
        } else {
            callback.onSuccess(result);
        }
    }


    final <S> Response<S> fetchAuthenticatedResponse(Callable<Response<S>> fetcher) {
        if (PreferenceUtils.getAuthToken() == null && !fetchAuthToken()) {
            return null;
        }

        Response<S> response = null;
        try {
            response = fetcher.call();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response == null) {
            return null;
        }

        // Refetch the auth token, if it's expired.
        if (response.code() == 401) {
            if (!fetchAuthToken()) {
                return null;
            }
            try {
                response = fetcher.call();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return response;
    }

    private boolean fetchAuthToken() {
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
            PreferenceUtils.setAuthToken(tokenResponse.body().token);
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

    final void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    final void setResult(T result) {
        this.result = result;
    }
}
