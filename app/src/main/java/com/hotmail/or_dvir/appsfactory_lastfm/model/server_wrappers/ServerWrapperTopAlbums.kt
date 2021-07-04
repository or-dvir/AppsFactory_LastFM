package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ServerWrapperTopAlbums(
    @Json(name = "topalbums")
    val topAlbums: TopAlbums?
)
{
    fun getTopAlbums() = topAlbums?.albums

    @JsonClass(generateAdapter = true)
    class TopAlbums(
        @Json(name = "album")
        val albums: List<Album> = listOf()
    )
}