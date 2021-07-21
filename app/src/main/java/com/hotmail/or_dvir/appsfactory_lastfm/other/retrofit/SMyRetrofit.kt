package com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit

import com.hotmail.or_dvir.appsfactory_lastfm.other.SMyMoshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * a singleton class to access retrofit functionality
 */
object SMyRetrofit
{
    private const val TIMEOUT_SECONDS: Long = 15

    /**
     * our access point to the LastFM api
     */
    internal val lastFmApi: ILastFmApi

    init
    {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ws.audioscrobbler.com/2.0/")
            .addConverterFactory(MoshiConverterFactory.create(SMyMoshi.instance).asLenient())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .readTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .connectTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .writeTimeout(TIMEOUT_SECONDS, TimeUnit.SECONDS)
                    .build()
            )
            .build()

        lastFmApi = retrofit.create(ILastFmApi::class.java)
    }
}