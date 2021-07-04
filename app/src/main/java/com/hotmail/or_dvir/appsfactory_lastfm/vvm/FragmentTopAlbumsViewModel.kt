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
    val albums = MutableLiveData<List<Album>?>(listOf())

    //do NOT initialize the error! if you do, the UI will respond when the view model is created
    val error = MutableLiveData<String>()

    fun getTopAlbums(artistName: String?)
    {
        if (artistName == null)
        {
            albums.value = null
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            albums.value = repoAlbums.getTopAlbums(artistName).getTopAlbums()
            isLoading.value = false
        }
    }

    //todo
    // when adding and removing albums to/from favorites, should i show a loading dialog
    //      on the album itself???
    fun addAlbumToFavorites(album: Album) = addOrRemoveAlbum(true, album)

    fun removeAlbumFromFavorites(album: Album) = addOrRemoveAlbum(false, album)

    private fun addOrRemoveAlbum(isAdding: Boolean, album: Album)
    {
        viewModelScope.launch(Dispatchers.Main) {
            val success = if (isAdding)
            {
                repoAlbums.addFavorite(album)
            } else
            {
                repoAlbums.removeFavorite(album)
            }

            if (!success)
            {
                val errorRes = if (isAdding)
                {
                    R.string.error_addingAlbum_s
                } else
                {
                    R.string.error_removingAlbum_s
                }

                error.value = getString(errorRes, album.name ?: "")
            }
        }
    }
}