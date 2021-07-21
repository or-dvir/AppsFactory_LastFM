package com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit

import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperAlbumDetails
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperArtistsSearch
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperTopAlbums
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * an interface representing our queries for the LastFm api.
 * used for Retrofit library.
 */
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
    /**
     * searches the LastFM api for artists with the given [searchQuery]
     */
    @GET("?method=artist.search&$API_PARAMS&limit=100")
    suspend fun search(@Query("artist") searchQuery: String): ServerWrapperArtistsSearch

    //for simplicity, we limit the results to the top 100 albums.
    //for the purposes of this demo app, and given that most artists will not even
    //reach this number, it's good enough.
    //for reference:
    // Nana Mouskouri, one of the artists with the most albums ever released, made 450 albums.
    // The Beatles have made 286 albums (source: wikipedia).
    //if we really wanted to display all the results, we would use google's Paging library.
    /**
     * searches the LastFM api for the top albums of the artist with the given [artistName]
     */
    @GET("?method=artist.gettopalbums&$API_PARAMS&limit=100")
    suspend fun getTopAlbums(@Query("artist") artistName: String): ServerWrapperTopAlbums

    /**
     * searches the LastFM api for detailed information about an album with the given
     * [albumName], by the given [artistName]
     */
    @GET("?method=album.getinfo&$API_PARAMS")
    suspend fun getAlbumDetails(
        @Query("artist") artistName: String,
        @Query("album") albumName: String
    ): ServerWrapperAlbumDetails
}
