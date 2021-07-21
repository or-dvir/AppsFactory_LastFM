package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.model.Image
import com.hotmail.or_dvir.appsfactory_lastfm.model.Tracks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A wrapper class for the servers' response for retrieving album details
 */
@JsonClass(generateAdapter = true)
data class ServerWrapperAlbumDetails(
    @Json(name = "album")
    val albumDetails: AlbumDetails?
)
{
    /**
     * converts this [ServerWrapperAlbumDetails] to [Album]
     */
    fun toAppAlbum(): Album?
    {
        return albumDetails?.let {
            Album(
                it.albumName,
                Artist(it.artistName),
                it.tracks,
                it.images
            )
        }
    }

    /**
     * a helper class which represent part of the servers' structure of results when
     * retrieving an album details
     */
    //cannot use existing Album class because it requires an Artist object, whereas here
    // the artist is only a string.
    @JsonClass(generateAdapter = true)
    data class AlbumDetails(
        @Json(name = "name")
        val albumName: String?,
        @Json(name = "artist")
        val artistName: String?,
        @Json(name = "image")
        val images: List<Image> = listOf(),
        @Json(name = "tracks")
        val tracks: Tracks?
    )
}