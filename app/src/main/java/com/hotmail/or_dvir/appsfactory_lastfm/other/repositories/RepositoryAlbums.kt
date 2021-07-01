package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperTopAlbums

abstract class RepositoryAlbums
{
    //region READ
    abstract suspend fun getTopAlbums(artistName: String): ServerWrapperTopAlbums
    //endregion

    //region WRITE
    abstract suspend fun addFavorite(albumId: String): Boolean
    abstract suspend fun removeFavorite(albumId: String): Boolean
    //endregion
}