package com.hotmail.or_dvir.appsfactory_lastfm.other

import android.app.Application
import android.util.Log
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.SMyDatabase
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums
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

    @Suppress("RemoveExplicitTypeArguments")
    val appModule = module {
        viewModel { FragmentSearchViewModel(androidApplication(), get()) }
        viewModel { FragmentTopAlbumsViewModel(androidApplication(), get()) }
        viewModel { FragmentAlbumDetailsViewModel(androidApplication(), get()) }
        viewModel { FragmentFavoriteAlbumsViewModel(androidApplication(), get()) }
        single<SMyDatabase> { SMyDatabase.getInstance(androidApplication()) }
        single<IDaoAlbums> { get<SMyDatabase>().daoAlbums() }
        single<RepositoryArtists> { RepositoryArtistsImpl(SMyRetrofit.lastFmApi) }
        single<RepositoryAlbums> {
            RepositoryAlbumsImpl(
                androidApplication(),
                appCoroutineScope,
                SMyRetrofit.lastFmApi,
                get()
            )
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, t ->
        Log.e(TAG, "a coroutine running under application scope threw an exception.", t)
    }

    /**
     * a [CoroutineScope] used to run coroutines that should not be cancelled
     */
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