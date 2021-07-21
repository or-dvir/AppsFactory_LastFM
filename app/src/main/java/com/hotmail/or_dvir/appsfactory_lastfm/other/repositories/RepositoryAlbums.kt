package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import androidx.lifecycle.LiveData
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperTopAlbums

/**
 * a repository class for [Album]s
 */
abstract class RepositoryAlbums
{
    //region READ
    /**
     * returns a [LiveData] object holding all of the users' favorite [Album]s
     */
    abstract fun getFavoriteAlbums(): LiveData<List<Album>>

    /**
     * returns whether or not an [Album] with the given [dbUUID] is marked as favorite
     */
    abstract suspend fun isInFavorites(dbUUID: String): Boolean

    /**
     * returns a server wrapper for a list of the top albums for the given [artistName]
     */
    abstract suspend fun getTopAlbums(artistName: String): ServerWrapperTopAlbums

    /**
     * searches detailed information about an album with the given [albumName]
     * by the given [artistName].
     * returns the result in the form of an [Album] object
     */
    abstract suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): Album?
    //endregion

    //region WRITE
    /**
     * adds the given [album] to the users' favorites list
     */
    abstract suspend fun addFavoriteAlbum(album: Album): Boolean

    /**
     * removes teh given [album] from the users' favorites list
     */
    abstract suspend fun deleteFavoriteAlbum(album: Album): Boolean
    //endregion
}