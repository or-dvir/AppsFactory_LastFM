package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import or_dvir.hotmail.com.dxutils.atLeastOneNull

class FragmentAlbumDetailsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    //todo add option to add/remove album to/from favorites?

    //do NOT initialize this with null or empty data! the UI observer will be triggered!
    val album = MutableLiveData<Album?>()

    //todo does this work as expected?
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

        //if we get here, artistName and albumName should not be null
        //due to the above "if" statement

        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            val serverAlbumDetails = repoAlbums.getAlbumDetails(artistName!!, albumName!!)
            album.value = serverAlbumDetails.toAppAlbum()
            isLoading.value = false
        }
    }
}