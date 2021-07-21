package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A wrapper class for the servers' response for retrieving top albums
 */
@JsonClass(generateAdapter = true)
class ServerWrapperTopAlbums(
    @Json(name = "topalbums")
    val topAlbums: TopAlbums?
)
{
    /**
     * returns the list of [Album]s contained in this [ServerWrapperTopAlbums]
     */
    fun getTopAlbums() = topAlbums?.albums

    /**
     * a helper class which represent part of the servers' structure of results when
     * searching for top albums
     */
    @JsonClass(generateAdapter = true)
    class TopAlbums(
        @Json(name = "album")
        val albums: List<Album> = listOf()
    )
}