package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FragmentTopAlbumsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    val albums = MutableLiveData<List<Album>?>(listOf())

    //do NOT initialize the error! if you do, the UI will respond when the view model is created
    val error = MutableLiveData<String>()
    var favoriteAlbums: List<Album>? = null

    fun getTopAlbumsAndLoadFavorites(artistName: String?)
    {
        if (artistName == null)
        {
            albums.value = null
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            //load top albums and favorites in parallel
            val favoriteDeferred = async { repoAlbums.getFavoriteAlbums() }
            val topDeferred = async { repoAlbums.getTopAlbums(artistName).getTopAlbums() }

            favoriteAlbums = favoriteDeferred.await()
            albums.value = topDeferred.await()

            i stopped here. now when top albums are in the recycler, favoriteAlbums field
            should be set and can be passed to the adapter
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
                repoAlbums.addFavoriteAlbum(album)
            } else
            {
                repoAlbums.deleteFavoriteAlbum(album)
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