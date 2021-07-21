package com.hotmail.or_dvir.appsfactory_lastfm.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A wrapper class for the servers' response representing a list of [Track]s
 */
@JsonClass(generateAdapter = true)
data class Tracks(
    @Json(name = "track")
    val trackList: List<Track> = listOf()
)
{
    /**
     * a helper class representing a single [Track] object as returned by the server
     */
    @JsonClass(generateAdapter = true)
    data class Track(
        @Json(name = "@attr")
        val attributes: Attr?,
        @Json(name = "name")
        val name: String?
    )

    /**
     * a helper class which represent the attributes of a [Track]
     */
    @JsonClass(generateAdapter = true)
    data class Attr(
        /**
         * the number of the [Track] in a given [Album]
         */
        @Json(name = "rank")
        val rank: Int?
    )
}