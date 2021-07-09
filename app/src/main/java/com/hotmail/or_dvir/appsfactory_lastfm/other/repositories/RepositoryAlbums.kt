package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperAlbumDetails
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperTopAlbums

abstract class RepositoryAlbums
{
    //region READ
    abstract suspend fun getFavoriteAlbums(): List<Album>
    abstract suspend fun getTopAlbums(artistName: String): ServerWrapperTopAlbums
    abstract suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): ServerWrapperAlbumDetails
    //endregion

    //region WRITE
    abstract suspend fun addFavoriteAlbum(album: Album): Boolean
    abstract suspend fun deleteFavoriteAlbum(album: Album): Boolean
    //endregion
}