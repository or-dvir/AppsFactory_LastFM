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

    //todo should probably make this observe a livedata query from room
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

        //we do not use the contains() function because that is based on Album.equals().
        //since Album is a data class, its' equals() function consists of all the fields in the
        //primary constructor, whereas 2 albums might be the same even with slightly varied fields
        //(e.g. from database and from lastFM API, since they don't exactly match).
        val isInFavorite = favoriteAlbums.find { it.dbUUID == album.dbUUID } != null

        var success: Boolean
        var error: String? = null
        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true

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