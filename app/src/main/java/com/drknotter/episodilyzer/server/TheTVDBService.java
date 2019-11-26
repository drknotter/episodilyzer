package com.drknotter.episodilyzer.server;

import com.drknotter.episodilyzer.model.AuthTokenRequest;
import com.drknotter.episodilyzer.model.AuthTokenResponse;
import com.drknotter.episodilyzer.server.model.SearchResult;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;

public interface TheTVDBService {
    String BASE_URL = "https://api.thetvdb.com";

    @POST("/login")
    void getAuthToken(@Body AuthTokenRequest authTokenRequest,
                      Callback<AuthTokenResponse> cb);

    @GET("/refresh_token")
    void getRefreshToken(@Header("Authorization") String bearerAuthToken,
                         Callback<AuthTokenResponse> cb);

    @GET("/search/series")
    void searchShows(@Header("Authorization") String bearerAuthToken,
                     @Query("name") String name,
                     Callback<SearchResult> cb);

    @GET("/api/{apiKey}/series/{seriesKey}/all/en.zip")
    @Streaming
    Response getSeriesInfo(@Path("apiKey") String apiKey,
                       @Path("seriesKey") int seriesKey);
}
