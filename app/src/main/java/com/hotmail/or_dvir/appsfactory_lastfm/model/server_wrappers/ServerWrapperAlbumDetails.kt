package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Image
import com.hotmail.or_dvir.appsfactory_lastfm.model.Tracks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ServerWrapperAlbumDetails(
    @Json(name = "name")
    val albumName: String?,
    @Json(name = "artist")
    val artistName: String?,
    @Json(name = "image")
    val images: List<Image> = listOf(),
    @Json(name = "tracks")
    val tracks: Tracks?
)
//todo BUG WITH MOSHI!!!!!!!!!!!!!!!!!!
// for some reason it cannot properly serialize the server response to this object!
// e.g. it claims that all the variables are missing in the server json, even though they are not!

//note: cannot use existing Album class because it requires an Artist object, whereas here
// the artist is only a string. Conversion will have to be done elsewhere