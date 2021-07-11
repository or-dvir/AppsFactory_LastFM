package com.hotmail.or_dvir.appsfactory_lastfm.other

import android.app.Application
import android.util.Log
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.SMyDatabase
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbumsImpl
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtists
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtistsImpl
import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.SMyRetrofit
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.FragmentAlbumDetailsViewModel
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.FragmentFavoriteAlbumsViewModel
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.FragmentSearchViewModel
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.FragmentTopAlbumsViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApplication : Application()
{
    companion object
    {
        private const val TAG = "MyApplication"
    }

    val appModule = module {
        //todo do i need to inject database? daos?
        viewModel { FragmentSearchViewModel(androidApplication(), get()) }
        viewModel { FragmentTopAlbumsViewModel(androidApplication(), get()) }
        viewModel { FragmentAlbumDetailsViewModel(androidApplication(), get()) }
        viewModel { FragmentFavoriteAlbumsViewModel(androidApplication(), get()) }
        single<RepositoryArtists> { RepositoryArtistsImpl(SMyRetrofit.lastFmApi) }
        single<RepositoryAlbums> { RepositoryAlbumsImpl(SMyRetrofit.lastFmApi, get()) }
        single { SMyDatabase.getInstance(androidApplication()) }
        single { get<SMyDatabase>().daoAlbums() }
    }

    private val exceptionHandler = CoroutineExceptionHandler { context, t ->
        //todo is it useful to print the context here? check what it prints
        Log.d(TAG, "a coroutine with $context failed.\n${t.message}")
    }

    //todo DONT DELETE!!! may be needed in the future
    private val appCoroutineScope = CoroutineScope(SupervisorJob() + exceptionHandler)

    override fun onCreate()
    {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(appModule)
        }
    }
}