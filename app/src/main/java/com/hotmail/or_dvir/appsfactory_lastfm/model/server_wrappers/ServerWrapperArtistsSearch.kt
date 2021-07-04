package com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers

import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ServerWrapperArtistsSearch(
    @Json(name = "results")
    val result: Result?
)
{
    fun getArtistsSearchResults() = result?.matches?.artists

    @JsonClass(generateAdapter = true)
    class Result(
        @Json(name = "artistmatches")
        val matches: ArtistMatches?
    )

    @JsonClass(generateAdapter = true)
    class ArtistMatches(
        @Json(name = "artist")
        val artists: List<Artist> = listOf()
    )
}