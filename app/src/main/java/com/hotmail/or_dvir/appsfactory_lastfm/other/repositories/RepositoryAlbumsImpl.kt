package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import android.app.Application
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.other.hasInternetConnection
import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class RepositoryAlbumsImpl(
    private val app: Application,
    private val appCoroutineScope: CoroutineScope,
    private val apiLastFM: ILastFmApi,
    private val daoAlbums: IDaoAlbums
) : RepositoryAlbums()
{
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
            if (app.hasInternetConnection())
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
    override suspend fun addFavoriteAlbum(album: Album): Boolean
    {
        return withContext(Dispatchers.IO) {
            //creating a new coroutine with the application scope to make sure
            //that this operation does not get cancelled if the original scope does.
            appCoroutineScope.async {
                //if we cannot uniquely identify the album, we cannot save it to the database
                album.apply {
                    if (!canBeStoredInDb())
                    {
                        return@async false
                    }

                    //tracks are missing. need to retrieve separately.
                    //can only do this if we have internet connection, and valid album/artist names
                    if (app.hasInternetConnection() &&
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
            }.await()
        }
    }

    override suspend fun deleteFavoriteAlbum(album: Album): Boolean
    {
        return withContext(Dispatchers.IO) {
            //creating a new coroutine with the application scope to make sure
            //that this operation does not get cancelled if the original scope does.
            appCoroutineScope.async {
                if (!album.canBeStoredInDb())
                {
                    //if an album cannot be stored in the db, there is nothing to delete.
                    //return TRUE for success
                    return@async true
                }

                //album.dbUUID should not be null because of the "if" statement above
                val numDeleted = daoAlbums.delete(album.dbUUID!!)
                numDeleted != 0
            }.await()
        }
    }

    override suspend fun isInFavorites(dbUUID: String) =
        withContext(Dispatchers.IO) {
            daoAlbums.isInFavorites(dbUUID)
        }

    override fun getFavoriteAlbums() = daoAlbums.getFavoriteAlbums()
}