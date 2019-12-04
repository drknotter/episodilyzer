package com.drknotter.episodilyzer.server;

import com.drknotter.episodilyzer.server.model.ActorList;
import com.drknotter.episodilyzer.server.model.AuthTokenRequest;
import com.drknotter.episodilyzer.server.model.AuthTokenResponse;
import com.drknotter.episodilyzer.server.model.BannerList;
import com.drknotter.episodilyzer.server.model.EpisodeList;
import com.drknotter.episodilyzer.server.model.SeriesResponse;
import com.drknotter.episodilyzer.server.model.SeriesSearchResultList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TheTVDBService {
    String API_URL = "https://api.thetvdb.com";
    String WEB_URL = "https://thetvdb.com";

    @POST("/login")
    Call<AuthTokenResponse> getAuthToken(@Body AuthTokenRequest authTokenRequest);

    @GET("/search/series")
    Call<SeriesSearchResultList> searchShows(@Header("Authorization") String bearerAuthToken,
                                             @Query("name") String name);

    @GET("/series/{seriesId}/images/query")
    Call<BannerList> getBanners(@Header("Authorization") String bearerAuthToken,
                                @Path("seriesId") int seriesId,
                                @Query("keyType") String type,
                                @Query("subKey") String subType);

    @GET("/series/{seriesId}")
    Call<SeriesResponse> getSeries(@Header("Authorization") String bearerAuthToken,
                                   @Path("seriesId") int seriesId);

    @GET("/series/{seriesId}/episodes")
    Call<EpisodeList> getEpisodes(@Header("Authorization") String bearerAuthToken,
                                  @Path("seriesId") int seriesId,
                                  @Query("page") Integer page);

    @GET("/series/{seriesId}/actors")
    Call<ActorList> getSeriesActors(@Header("Authorization") String bearerAuthToken,
                                    @Path("seriesId") int seriesId);

}
