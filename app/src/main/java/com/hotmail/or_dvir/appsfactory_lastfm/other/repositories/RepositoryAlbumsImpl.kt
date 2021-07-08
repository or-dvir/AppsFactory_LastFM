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
    override suspend fun getTopAlbums(artistName: String) =
        withContext(Dispatchers.IO) {
            lastFmApi.getTopAlbums(artistName)
        }

    override suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): ServerWrapperAlbumDetails =
        withContext(Dispatchers.IO) {
            lastFmApi.getAlbumDetails(artistName, albumName)
        }

    //todo MUST FINISH no matter what!
    override suspend fun addFavoriteAlbum(album: Album) =
        withContext(Dispatchers.IO) {
            val rowId = albumsDao.insert(album)
            rowId != -1L
        }

    //todo MUST FINISH no matter what!
    override suspend fun deleteFavoriteAlbum(album: Album) =
        withContext(Dispatchers.IO) {
            val numDeleted = albumsDao.delete(album)
            numDeleted != 0
        }
}