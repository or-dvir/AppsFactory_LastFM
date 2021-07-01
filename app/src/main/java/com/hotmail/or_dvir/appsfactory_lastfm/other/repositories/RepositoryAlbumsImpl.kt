package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.ILastFmApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RepositoryAlbumsImpl(private val lastFmApi: ILastFmApi) : RepositoryAlbums()
{
    //todo try all kinds of scenarios and handle errors appropriately
    //todo the api has a list of error codes. handle them!!!
    override suspend fun getTopAlbums(artistName: String) =
        withContext(Dispatchers.IO) {
            lastFmApi.getTopAlbums(artistName)
        }

    override suspend fun addFavorite(albumId: String): Boolean
    {
        TODO("not implemented")
    }

    override suspend fun removeFavorite(albumId: String): Boolean
    {
        TODO("not implemented")
    }
}