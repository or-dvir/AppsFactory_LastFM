package com.hotmail.or_dvir.appsfactory_lastfm.other

import com.squareup.moshi.Moshi

/**
 * a singleton class holding an instance of Moshi, allowing us to access Moshi functionality
 */
object SMyMoshi
{
    internal val instance: Moshi = Moshi.Builder().build()
}