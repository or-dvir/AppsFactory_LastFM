package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseAlbumsViewModel(
    app: Application,
    internal val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    /**
     * @return String? error string, or null if successful
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