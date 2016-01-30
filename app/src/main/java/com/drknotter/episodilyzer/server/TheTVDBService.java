package com.drknotter.episodilyzer.server;

import com.drknotter.episodilyzer.server.model.SearchResult;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.http.Streaming;

public interface TheTVDBService {
    String BASE_URL = "http://thetvdb.com";

    @POST("/api/GetSeries.php")
    void searchShows(@Query("seriesname") String name,
                     Callback<SearchResult> cb);

    @GET("/api/{apiKey}/series/{seriesKey}/all/en.zip")
    @Streaming
    Response getSeriesInfo(@Path("apiKey") String apiKey,
                       @Path("seriesKey") int seriesKey);
}
