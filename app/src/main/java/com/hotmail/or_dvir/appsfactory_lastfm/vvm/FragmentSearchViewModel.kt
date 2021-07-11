package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtists
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FragmentSearchViewModel(
    app: Application,
    private val repoArtists: RepositoryArtists
) : BaseAndroidViewModel(app)
{
    private companion object
    {
        private const val TAG = "FragmentSearchViewModel"
    }

    var hasSearchedBefore = false
        private set

    val artists: MutableLiveData<List<Artist>?> = MutableLiveData(listOf())
    var lastSearchQuery = ""
        private set

    fun searchArtists(query: String)
    {
        val exceptionHandler = CoroutineExceptionHandler { _, t ->
            Log.e(TAG, t.message, t)
            artists.value = null
            isLoading.value = false
        }

        hasSearchedBefore = true
        lastSearchQuery = query
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isLoading.value = true
            artists.value = repoArtists.searchArtists(query).getArtistsSearchResults()
            isLoading.value = false
        }
    }
}