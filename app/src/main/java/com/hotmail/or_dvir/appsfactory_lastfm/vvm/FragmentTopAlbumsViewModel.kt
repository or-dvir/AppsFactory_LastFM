package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentTopAlbumsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    private companion object {
        private const val TAG = "FragTopAlbumsVM"
    }

    val topAlbums = MutableLiveData<List<Album>?>(listOf())

    fun loadTopAlbums(artistName: String?)
    {
        if (artistName == null)
        {
            topAlbums.value = null
            return
        }

        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e(TAG, t.message, t)
            topAlbums.value = null
            isLoading.value = false
        }

        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isLoading.value = true
            topAlbums.value = repoAlbums.getTopAlbums(artistName).getTopAlbums()
            isLoading.value = false
        }
    }

    /**
     * @return String? error string, or null if successful
     */
    //using a listener and not making this a suspend function because it will be called
    //from the fragment with the fragment lifecycle scope. so if the fragment dies,
    //it will also cancel coroutines started here (as child coroutines of the fragment scope)
    fun addOrRemoveAlbum(album: Album, onFinish: (String?) -> Unit)
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