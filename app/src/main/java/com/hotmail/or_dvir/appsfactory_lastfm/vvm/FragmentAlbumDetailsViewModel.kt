package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentAlbumDetailsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    //todo add option to add/remove album to/from favorites?
    
    //do NOT initialize this with null or empty data! the UI observer will be triggered!
    //todo observe me!
    val album = MutableLiveData<Album>()

    //todo does this work as expected?
    fun getAlbumTracksAsListText(): String?
    {
        return album.value?.let { album ->
            album.getTracks()?.joinToString(
                separator = "\n",
                transform = { track ->
                    "${track.attributes.rank}. "
                }
            )
        }
    }

    fun getAlbumDetails(artistName: String, albumName: String)
    {
        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            album.value = repoAlbums.getAlbumDetails(artistName, albumName).album
            isLoading.value = false
        }
    }
}