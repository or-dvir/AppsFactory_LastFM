package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryArtistsImpl(private val lastFmApi: ILastFmApi) : RepositoryArtists()
{
    //todo try all kinds of scenarios and handle errors appropriately
    override suspend fun searchArtists(searchQuery: String) =
        withContext(Dispatchers.IO) {
            lastFmApi.search(searchQuery)
        }
}