package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist

abstract class RepositoryArtists
{
    //region READ
    abstract suspend fun searchArtists(searchQuery: String): List<Artist>
    //endregion

    //region WRITE
    abstract suspend fun addFavorite(artistId: String): Boolean
    abstract suspend fun removeFavorite(artistId: String): Boolean
    //endregion
}