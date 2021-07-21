package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * A wrapper class for the servers' response for retrieving search results for artists
 */
@JsonClass(generateAdapter = true)
class ServerWrapperArtistsSearch(
    @Json(name = "results")
    val result: Result?
)
{
    /**
     * returns the list of [Artist]s contained in this [ServerWrapperArtistsSearch]
     */
    fun getArtistsSearchResults() = result?.matches?.artists

    /**
     * a helper class which represent part of the servers' structure of results when
     * searching for an [Artist]
     */
    @JsonClass(generateAdapter = true)
    class Result(
        @Json(name = "artistmatches")
        val matches: ArtistMatches?
    )

    /**
     * a helper class which represent part of the servers' structure of results when
     * searching for an [Artist]. This class holds teh actual list of [Artist]s
     */
    @JsonClass(generateAdapter = true)
    class ArtistMatches(
        @Json(name = "artist")
        val artists: List<Artist> = listOf()
    )
}