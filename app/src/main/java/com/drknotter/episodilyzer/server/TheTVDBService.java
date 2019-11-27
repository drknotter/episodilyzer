package com.drknotter.episodilyzer.server;

import com.drknotter.episodilyzer.model.AuthTokenRequest;
import com.drknotter.episodilyzer.model.AuthTokenResponse;
import com.drknotter.episodilyzer.server.model.SearchResult;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

public interface TheTVDBService {
    String API_URL = "https://api.thetvdb.com";
    String WEB_URL = "https://thetvdb.com";

    @POST("/login")
    Call<AuthTokenResponse> getAuthToken(@Body AuthTokenRequest authTokenRequest);

    @GET("/search/series")
    Call<SearchResult> searchShows(@Header("Authorization") String bearerAuthToken,
                     @Query("name") String name);

    @GET("/api/{apiKey}/series/{seriesKey}/all/en.zip")
    @Streaming
    Response getSeriesInfo(@Path("apiKey") String apiKey,
                           @Path("seriesKey") int seriesKey);
}
