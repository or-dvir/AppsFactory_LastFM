package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAlbumsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * a view model holding information on top albums of an Artist
 */
class FragmentTopAlbumsViewModel(
    app: Application,
    repoAlbums: RepositoryAlbums
) : BaseAlbumsViewModel(app, repoAlbums)
{
    private companion object
    {
        private const val TAG = "FragTopAlbumsVM"
    }

    /**
     * the top albums currently held by this [FragmentTopAlbumsViewModel]
     */
    val topAlbums = MutableLiveData<List<Album>?>(listOf())

    /**
     * searches for the top albums of the artist with the given [artistName]
     * the result would be set in [topAlbums]
     */
    fun loadTopAlbums(artistName: String?)
    {
        if (artistName == null)
        {
            topAlbums.value = null
            return
        }

        val exceptionHandler = createCoroutineExceptionHandler(TAG) { topAlbums.value = null }

        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isLoading.value = true
            topAlbums.value = repoAlbums.getTopAlbums(artistName).getTopAlbums()
            isLoading.value = false
        }
    }
}