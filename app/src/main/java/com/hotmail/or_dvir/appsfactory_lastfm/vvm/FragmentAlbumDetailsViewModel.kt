package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAlbumsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import or_dvir.hotmail.com.dxutils.atLeastOneNull

/**
 * a view model holding detailed information about a specific album
 */
class FragmentAlbumDetailsViewModel(
    app: Application,
    repoAlbums: RepositoryAlbums
) : BaseAlbumsViewModel(app, repoAlbums)
{
    private companion object
    {
        private const val TAG = "FragAlbumDetailsVM"
    }

    /**
     * detailed information about the album held by this [FragmentAlbumDetailsViewModel]
     */
    //do NOT initialize this with null or empty data! the UI observer will be triggered!
    val album = MutableLiveData<Album?>()

    /**
     * retrieves a numbered list of the tracks of [album], represented as a single string,
     * or null if creating such a list is not possible.
     *
     * the returned string will look like this (if not `null`):
     * 1. song 1
     * 2. song 2
     * 3. song 3
     *
     * and so forth...
     *
     * note that if the song number cannot be determined, only the song name will be included
     */
    fun getAlbumTracksAsListText(): String?
    {
        return album.value?.let { album ->
            album.getTracks()?.joinToString(
                separator = "\n",
                transform = { track ->
                    val trackNumber = track.attributes?.rank
                    val trackName = track.name

                    StringBuilder().apply {
                        trackNumber?.let { append("$it. ") }
                        trackName?.let { append(it) }
                    }.toString()
                }
            )
        }
    }

    /**
     * returns whether or not [album] is marked as favorite
     */
    suspend fun isInFavorites(): Boolean
    {
        return album.value?.let {
            it.canBeStoredInDb() && repoAlbums.isInFavorites(it.dbUUID!!)
        } ?: false
    }

    /**
     * calls [BaseAlbumsViewModel.addOrRemoveAlbumFavorites] with [album].
     * If [album] is null, this function does nothing and immediately
     * invokes [onFinish] with a null error (nothing happened, so technically there was no error)
     *
     */
    fun addOrRemoveAlbumFavorites(onFinish: (String?) -> Unit)
    {
        if (album.value == null)
        {
            onFinish.invoke(null)
        } else
        {
            //album.value is not null because of the "if" above
            super.addOrRemoveAlbumFavorites(album.value!!, onFinish)
        }
    }

    /**
     * loads the details of an album with the given [albumName] and [artistName]
     *
     * the result will be sent to [album]
     */
    fun getAlbumDetails(artistName: String?, albumName: String?)
    {
        //note: these values are used in the albums repository to retrieve data from the internet
        //and local database (see Album.dbUUID).
        //if one of these fields is null, we cannot identify this album.
        //lastFM api also allows to get information via the id, but we are not using this field
        //in this app.
        if (atLeastOneNull(artistName, albumName))
        {
            album.value = null
            return
        }

        val exceptionHandler = createCoroutineExceptionHandler(TAG) { album.value = null }

        //if we get here, artistName and albumName should not be null
        //due to the above "if" statement
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isLoading.value = true
            album.value = repoAlbums.getAlbumDetails(artistName!!, albumName!!)
            isLoading.value = false
        }
    }
}