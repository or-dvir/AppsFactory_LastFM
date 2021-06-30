package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtists
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentSearchViewModel(
    app: Application,
    private val repoArtists: RepositoryArtists
) : BaseAndroidViewModel(app)
{
    val artists = MutableLiveData<List<Artist>>()

    fun searchArtists(query: String)
    {
        viewModelScope.launch(Dispatchers.Main) {
            isLoading.value = true
            artists.value = repoArtists.searchArtists(query).getArtistsSearchResults()
            isLoading.value = false
        }
    }
}