package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperAlbumDetails
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryAlbumsImpl(
    private val lastFmApi: ILastFmApi,
    private val albumsDao: IDaoAlbums
) : RepositoryAlbums()
{
    //todo try all kinds of scenarios and handle errors appropriately
    //todo the api has a list of error codes. handle them!!!
    //todo how do i keep the information in the database up to date?
    //      if the downloaded album is in my favorites, also update it?
    //      maybe for simplicity just say that the information is only updated
    //      if the user removes and saves the information again
    override suspend fun getTopAlbums(artistName: String) =
        withContext(Dispatchers.IO) {
            lastFmApi.getTopAlbums(artistName)
        }

    //todo this should work offline!
    // if no internet connection, load from database
    override suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): ServerWrapperAlbumDetails =
        withContext(Dispatchers.IO) {
            lastFmApi.getAlbumDetails(artistName, albumName)
        }

    //todo MUST FINISH no matter what!
    override suspend fun addFavoriteAlbum(album: Album) =
        //todo should do nothing if already exists
        withContext(Dispatchers.IO) {
            //if we cannot uniquely identify the album, we cannot save it to the database
            if (album.canBeStoredInDb())
            {
                val rowId = albumsDao.insert(album)
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
                val numDeleted = albumsDao.delete(album.dbUUID!!)
                numDeleted != 0
            } else
            {
                //if an album cannot be stored in the db, there is nothing to delete.
                //return TRUE for success
                true
            }
        }
}