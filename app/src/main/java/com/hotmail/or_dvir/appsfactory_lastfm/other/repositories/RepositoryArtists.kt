package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperArtistsSearch

abstract class RepositoryArtists
{
    abstract suspend fun searchArtists(searchQuery: String): ServerWrapperArtistsSearch
}