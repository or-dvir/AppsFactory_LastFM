package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Image
import com.hotmail.or_dvir.appsfactory_lastfm.model.Tracks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ServerWrapperAlbumDetails(
    @Json(name = "name")
    val albumName: String,
    @Json(name = "artist")
    val artistName: String,
    @Json(name = "image")
    val images: List<Image> = listOf(),
    @Json(name = "tracks")
    val tracks: Tracks?,
    @Json(name = "mbid")
    val id: String
)
//note: cannot use existing Album class because it requires an Artist object, whereas here
// the artist is only a string. Conversion will have to be done elsewhere