package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtists
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseAndroidViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * a view model holding information about search results for an [Artist]
 */
class FragmentSearchViewModel(
    app: Application,
    private val repoArtists: RepositoryArtists
) : BaseAndroidViewModel(app)
{
    private companion object
    {
        private const val TAG = "FragmentSearchViewModel"
    }

    /**
     * a helper field telling us whether or not this is the first search performed by the user
     */
    var hasSearchedBefore = false
        private set

    /**
     * holds the search results of the last query performed by the user
     */
    val artists: MutableLiveData<List<Artist>?> = MutableLiveData(listOf())

    /**
     * a helper field holding the last artist the user searched for
     */
    var lastSearchQuery = ""
        private set

    /**
     * searches for artists matching the given [query]
     *
     * the results would be sent to [artists]
     */
    fun searchArtists(query: String)
    {
        val exceptionHandler = createCoroutineExceptionHandler(TAG) { artists.value = null }

        hasSearchedBefore = true
        lastSearchQuery = query
        viewModelScope.launch(Dispatchers.Main + exceptionHandler) {
            isLoading.value = true
            artists.value = repoArtists.searchArtists(query).getArtistsSearchResults()
            isLoading.value = false
        }
    }
}