package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentTopAlbumsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    val topAlbums = MutableLiveData<List<Album>?>(listOf())

    fun loadTopAlbums(artistName: String?)
    {
        if (artistName == null)
        {
            topAlbums.value = null
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
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

            //todo do i have to observe this first to make it work?
            val isInFavorite = repoAlbums.getFavoriteAlbums().value?.contains(album) ?: false
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