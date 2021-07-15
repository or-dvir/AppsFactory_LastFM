package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import android.app.Application
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.other.hasInternetConnection
import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryAlbumsImpl(
    private val application: Application,
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

    override suspend fun getAlbumDetails(
        artistName: String,
        albumName: String
    ): Album?
    {
        return withContext(Dispatchers.IO) {
            if (application.hasInternetConnection())
            {
                apiLastFM.getAlbumDetails(artistName, albumName).toAppAlbum()
            } else
            {
                //no internet. load from database
                daoAlbums.getAlbum(Album.toDbUuid(artistName, albumName))
            }
        }
    }

    //note: for simplicity, the information saved in the DB will NOT be automatically updated.
    //to get the latest information about an album, it must be removed from favorites
    // and then re-added
    //todo MUST FINISH no matter what!
    override suspend fun addFavoriteAlbum(album: Album): Boolean
    {
        return withContext(Dispatchers.IO) {
            //if we cannot uniquely identify the album, we cannot save it to the database
            album.apply {
                if (!canBeStoredInDb())
                {
                    return@withContext false
                }

                //tracks are missing. need to retrieve separately.
                //can only do this if we have internet connection, and valid album/artist names
                if (application.hasInternetConnection() &&
                    tracks == null &&
                    isNameValid() &&
                    isArtistNameValid()
                )
                {
                    //artist/album name should not be null because we are checking
                    //they are valid in the "if" above
                    tracks = getAlbumDetails(artist!!.name!!, name!!)?.tracks
                }
            }

            val rowId = daoAlbums.insert(album)
            rowId != -1L
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