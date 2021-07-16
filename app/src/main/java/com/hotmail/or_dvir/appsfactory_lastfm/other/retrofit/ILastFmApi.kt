package com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit

import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperAlbumDetails
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperArtistsSearch
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperTopAlbums
import retrofit2.http.GET
import retrofit2.http.Query

interface ILastFmApi
{
    companion object
    {
        private const val API_KEY = "f6a88da25c6b36d76e16e89f074a7478"
        private const val API_PARAMS = "api_key=$API_KEY&format=json"
    }

    //instead of implementing paging, we limit the search query to 100 results.
    //not only does this mean less code (and therefore less potential bugs),
    //but it also makes sense. if the user cannot find the artist they were looking for
    //in the first 100 results, they probably need to adjust their search query.
    //if we really wanted to display all the results, we would use google's Paging library
    @GET("?method=artist.search&$API_PARAMS&limit=100")
    suspend fun search(@Query("artist") searchQuery: String): ServerWrapperArtistsSearch

    //for simplicity, we limit the results to the top 100 albums.
    //for the purposes of this demo app, it's good enough.
    //if we really wanted to display all the results, we would use google's Paging library
    @GET("?method=artist.gettopalbums&$API_PARAMS&limit=100")
    suspend fun getTopAlbums(@Query("artist") artistName: String): ServerWrapperTopAlbums

    @GET("?method=album.getinfo&$API_PARAMS")
    suspend fun getAlbumDetails(
        @Query("artist") artistName: String,
        @Query("album") albumName: String
    ): ServerWrapperAlbumDetails
}
