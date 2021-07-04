package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tracks(
    @Json(name = "track")
    val trackList: List<Track>
)
{
    @JsonClass(generateAdapter = true)
    data class Track(
        @Json(name = "@attr")
        val attributes: Attr,
        @Json(name = "name")
        val name: String
    )

    @JsonClass(generateAdapter = true)
    data class Attr(
        @Json(name = "rank")
        val rank: Int
    )
}