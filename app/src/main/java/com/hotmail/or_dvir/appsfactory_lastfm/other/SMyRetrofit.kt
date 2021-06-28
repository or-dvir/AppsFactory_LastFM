package com.hotmail.or_dvir.appsfactory_lastfm.other

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object SMyRetrofit
{
    private const val TIMEOUT_SECONDS: Long = 15
    //todo keep for reference.
    // remove when not longer needed
//    val countriesClient: ICountriesClient

    init
    {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        //todo keep for reference.
        // remove when not longer needed
//        countriesClient = retrofit.create<ICountriesClient>(ICountriesClient::class.java)
    }
}