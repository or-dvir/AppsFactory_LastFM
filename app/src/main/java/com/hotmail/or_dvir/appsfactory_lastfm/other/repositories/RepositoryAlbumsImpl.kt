package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperAlbumDetails
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryAlbumsImpl(
    private val apiLastFM: ILastFmApi,
    private val daoAlbums: IDaoAlbums
) : RepositoryAlbums()
{
    //todo try all kinds of scenarios and handle errors appropriately
    //todo the api has a list of error codes. handle them!!!

    override suspend fun getTopAlbums(artistName: String) =
        withContext(Dispatchers.IO) {
            apiLastFM.getTopAlbums(artistName)
        }

    //todo this should work offline!
    // if no internet connection, load from database
    override suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): ServerWrapperAlbumDetails =
        withContext(Dispatchers.IO) {
            apiLastFM.getAlbumDetails(artistName, albumName)
        }

    //note: for simplicity, the information saved in the DB will NOT be automatically updated.
    //to get the latest information about an album, it must be removed from favorites
    // and then re-added
    //todo MUST FINISH no matter what!
    override suspend fun addFavoriteAlbum(album: Album) =
        withContext(Dispatchers.IO) {
            //if we cannot uniquely identify the album, we cannot save it to the database
            if (album.canBeStoredInDb())
            {
                val rowId = daoAlbums.insert(album)
                rowId != -1L
            } else
            {
                false
            }
        }

    //todo MUST FINISH no matter what!
    override suspend fun deleteFavoriteAlbum(album: Album) =
        withContext(Dispatchers.IO) {
            if (album.canBeStoredInDb())
            {
                //album.dbUUID should not be null because of the "if" statement
                val numDeleted = daoAlbums.delete(album.dbUUID!!)
                numDeleted != 0
            } else
            {
                //if an album cannot be stored in the db, there is nothing to delete.
                //return TRUE for success
                true
            }
        }

    override suspend fun isInFavorites(dbUUID: String) =
        withContext(Dispatchers.IO) {
            daoAlbums.isInFavorites(dbUUID)
        }

    override fun getFavoriteAlbums() = daoAlbums.getFavoriteAlbums()
}