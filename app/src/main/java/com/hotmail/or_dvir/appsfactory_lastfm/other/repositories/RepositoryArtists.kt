package com.hotmail.or_dvir.appsfactory_lastfm.other.repositories

import com.hotmail.or_dvir.appsfactory_lastfm.model.server_wrappers.ServerWrapperArtistsSearch

/**
 * a repository class for Artists
 */
abstract class RepositoryArtists
{
    /**
     * returns a list of artists matching the given [searchQuery] in the form of
     * [ServerWrapperArtistsSearch]
     */
    abstract suspend fun searchArtists(searchQuery: String): ServerWrapperArtistsSearch
}