package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * a base class holding some shared functionality for view models handling [Album]s
 */
abstract class BaseAlbumsViewModel(
    app: Application,
    internal val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    /**
     * adds or removes the given [album] to/from the database.
     * the function automatically determines whether [album] needs to be added or removed
     * based on whether it is already in the favorites list or not.
     *
     * if this album cannot be saved in the database, this function immediately triggers
     * [onFinish] and finishes
     *
     * @param onFinish a listener to be invoked when the operation finishes.
     * the string parameter of this listener represents an error that occurred
     * during the insert/delete operation. if this value is `null`, the operation was successful
     *
     * @see [Album.canBeStoredInDb]
     */
    //using a listener and not making this a suspend function because it will be called
    //from the fragment with the fragment lifecycle scope. so if the fragment dies,
    //it will also cancel coroutines started here (as child coroutines of the fragment scope)
    fun addOrRemoveAlbumFavorites(album: Album, onFinish: (String?) -> Unit)
    {
        if (!album.canBeStoredInDb())
        {
            onFinish(getString(R.string.error_cannotAddAlbumToFavorites))
            return
        }

        var success: Boolean
        var error: String? = null
        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true

            //album.dbUUID should not be null here because it's the first thing
            //we check in this function
            val isInFavorite = repoAlbums.isInFavorites(album.dbUUID!!)
            if (isInFavorite)
            {
                //removing album from favorites
                success = repoAlbums.deleteFavoriteAlbum(album)
                if (!success)
                {
                    error = getString(R.string.error_removingAlbum)
                }
            } else
            {
                //adding album to favorites
                success = repoAlbums.addFavoriteAlbum(album)
                if (!success)
                {
                    error = getString(R.string.error_addingAlbum)
                }
            }

            isLoading.value = false
            onFinish(error)
        }
    }
}