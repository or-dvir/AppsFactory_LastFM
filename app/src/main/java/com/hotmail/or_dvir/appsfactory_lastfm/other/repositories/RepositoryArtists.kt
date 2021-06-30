package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperArtistsSearch

abstract class RepositoryArtists
{
    //region READ
    abstract suspend fun searchArtists(searchQuery: String): ServerWrapperArtistsSearch
    //endregion

    //region WRITE
    abstract suspend fun addFavorite(artistId: String): Boolean
    abstract suspend fun removeFavorite(artistId: String): Boolean
    //endregion
}