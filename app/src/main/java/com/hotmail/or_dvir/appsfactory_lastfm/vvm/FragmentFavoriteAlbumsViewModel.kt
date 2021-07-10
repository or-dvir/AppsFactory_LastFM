package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentFavoriteAlbumsViewModel(
    app: Application,
    private val repoAlbums: RepositoryAlbums
) : BaseAndroidViewModel(app)
{
    val favoriteAlbums = repoAlbums.getFavoriteAlbums()

    //using a listener and not making this a suspend function because it will be called
    //from the fragment with the fragment lifecycle scope. so if the fragment dies,
    //it will also cancel coroutines started here (as child coroutines of the fragment scope)
    fun removeAlbum(album: Album, onFinish: (Boolean) -> Unit)
    {
        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            val success = repoAlbums.deleteFavoriteAlbum(album)
            isLoading.value = false
            onFinish(success)
        }
    }
}