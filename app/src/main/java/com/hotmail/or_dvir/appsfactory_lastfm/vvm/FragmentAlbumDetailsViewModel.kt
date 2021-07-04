package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperAlbumDetails
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
        //todo can theoretically use id (if exists) instead.
        // but there is no time for this... just add a note in the documentation
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
            album.value = convertServerAlbumToAppAlbum(serverAlbumDetails)
            isLoading.value = false
        }
    }

    private fun convertServerAlbumToAppAlbum(serverAlbum: ServerWrapperAlbumDetails) =
        Album(
            serverAlbum.id,
            serverAlbum.albumName,
            //todo is empty id ok here? better idea?
            Artist("", serverAlbum.artistName),
            serverAlbum.images,
            serverAlbum.tracks
        )
}