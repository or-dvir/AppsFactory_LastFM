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

    //todo add pagination for all operations!!!

    @GET("?method=artist.search&$API_PARAMS")
    suspend fun search(@Query("artist") searchQuery: String): ServerWrapperArtistsSearch

    @GET("?method=artist.gettopalbums&$API_PARAMS")
    suspend fun getTopAlbums(@Query("artist") artistName: String): ServerWrapperTopAlbums

    @GET("?method=album.getinfo&$API_PARAMS")
    suspend fun getAlbumDetails(
        @Query("artist") artistName: String,
        @Query("album") albumName: String
    ): ServerWrapperAlbumDetails
}
