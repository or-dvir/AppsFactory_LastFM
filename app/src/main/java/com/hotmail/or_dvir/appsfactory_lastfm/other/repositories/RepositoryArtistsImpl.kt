package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryArtistsImpl(private val lastFmApi: ILastFmApi) : RepositoryArtists()
{
    //todo try all kinds of scenarios and handle errors appropriately
    //todo the api has a list of error codes. handle them!!!
    override suspend fun searchArtists(searchQuery: String) =
        withContext(Dispatchers.IO) {
            lastFmApi.search(searchQuery)
        }

    override suspend fun addFavorite(artistId: String): Boolean
    {
        TODO("not implemented")
    }

    override suspend fun removeFavorite(artistId: String): Boolean
    {
        TODO("not implemented")
    }
}