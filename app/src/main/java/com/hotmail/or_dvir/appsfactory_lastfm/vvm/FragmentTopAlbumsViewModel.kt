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
    val topAlbums = MutableLiveData<List<Album>?>(listOf())

    //do NOT initialize the error! if you do, the UI will respond when the view model is created
    //todo scenario: say we had an error, fragment goes to background, fragment comes back
    //      to foreground. live data will trigger and we will see the same error again!
    val error = MutableLiveData<String>()
    var favoriteAlbums = listOf<Album>()

    fun getTopAlbumsAndLoadFavorites(artistName: String?)
    {
        if (artistName == null)
        {
            topAlbums.value = null
            return
        }

        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            //load top albums and favorites in parallel.
            //we load favorite albums here so we can pass it on later to the albums adapter
            //so that we may display the correct "favorite" icon to the user.
            //it is more efficient to query for the favorites here than in the adapter
            val favoriteDeferred = async { repoAlbums.getFavoriteAlbums() }
            val topDeferred = async { repoAlbums.getTopAlbums(artistName).getTopAlbums() }

            //todo is this the best place to load favorite albums?
            // maybe in the fragment before updating the adapter?
            favoriteAlbums = favoriteDeferred.await()
            topAlbums.value = topDeferred.await()

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
            isLoading.value = true

            val success =
                if (isAdding)
                {
                    repoAlbums.addFavoriteAlbum(album)
                } else
                {
                    repoAlbums.deleteFavoriteAlbum(album)
                }

            isLoading.value = false

            if (!success)
            {
                val errorRes =
                    if (isAdding)
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