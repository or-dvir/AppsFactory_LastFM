package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * a view model holding information about the users' favorite albums
 */
class FragmentFavoriteAlbumsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    private companion object
    {
        private const val TAG = "FragFavoriteAlbumsVM"
    }

    /**
     * a list of all the users' favorite albums
     */
    val favoriteAlbums = repoAlbums.getFavoriteAlbums()

    init
    {
        //the favorites fragment is immediately observing favoriteAlbums
        // so it should show the loading view immediately.
        // since it is observed directly from the database, it's the fragments'
        // responsibility to dismiss the loading view when needed
        isLoading.value = true
    }

    //using a listener and not making this a suspend function because it will be called
    // from the fragment with the fragment lifecycle scope. so if the fragment dies,
    // it will also cancel coroutines started here (as child coroutines of the fragment scope).
    //note the we are deliberately NOT using BaseAndroidViewModel.addOrRemoveAlbumFavorites()
    // because in this view model we know extra details about the album which makes the
    // implementation of this function much simpler
    /**
     * removes the given [album] from the users' favorites list.
     *
     * @param onFinish a listener to be invoked when the operation finishes.
     * the boolean parameter of this listener represents whether the operation succeeded or failed.
     */
    fun removeAlbum(album: Album, onFinish: (Boolean) -> Unit)
    {
        viewModelScope.launch(Dispatchers.Main + createCoroutineExceptionHandler(TAG)) {
            isLoading.value = true
            val success = repoAlbums.deleteFavoriteAlbum(album)
            isLoading.value = false
            onFinish(success)
        }
    }
}