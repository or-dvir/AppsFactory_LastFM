package com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit

import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ILastFmApi
{
    companion object
    {
        private const val API_KEY = "f6a88da25c6b36d76e16e89f074a7478"
        private const val API_PARAMS = "api_key=$API_KEY&format=json"
    }

    //todo do i need slash here?
    //todo defaults to first page and first 30 artists! add pagination!
//    @GET("/?method=artist.search&{searchQuery}&$API_PARAMS")
    @GET("/?method=artist.search&$API_PARAMS")
    suspend fun search(@Query("artist") searchQuery: String): List<Artist>

    //todo keep for reference.
    // remove when no longer needed
//    @GET("/shows/{showId}?embed[]=seasons&embed[]=episodes")
//    fun getMainInfoForShowWithSeasonsAndEpisodes(@Path("showId") showId: Long): Call<TvShowEntity>
//    @GET("/shows/{showId}/cast")
//    fun getCastForShow(@Path("showId") showId: Long): Call<List<CastCrewMember>>
//    @GET("/shows/{showId}/crew")
//    fun getCrewForShow(@Path("showId") showId: Long): Call<List<CastCrewMember>>
//    @GET("/search/shows")
//    fun searchForShow(@Query("q") searchQuery: String): Call<List<ShowSearchResult>>
//    @GET("/seasons/{seasonId}/episodes")
//    fun getAllEpisodesForSeason(@Path("seasonId") seasonId: Long): Call<List<EpisodeEntity>>
//    @GET("/shows/{showId}/seasons")
//    fun getAllSeasons(@Path("showId") showId: Long): Call<List<SeasonEntity>>
//    @GET("/seasons/{seasonId}/episodes")
//    fun getEpisodesForSeasons(@Path("seasonId") seasonId: Long): Call<List<EpisodeEntity>>
}
