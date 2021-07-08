package com.hotmail.or_dvir.appsfactory_lastfm.other

import android.app.Application
import android.util.Log
import androidx.annotation.VisibleForTesting
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.SMyDatabase
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbumsImpl
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtists
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryArtistsImpl
import com.hotmail.or_dvir.appsfactory_lastfm.other.retrofit.SMyRetrofit
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.FragmentAlbumDetailsViewModel
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

    //todo keep for reference. delete if unused
    @VisibleForTesting
    val appModule = module {
        //todo do i need to inject database? daos?
        viewModel { FragmentSearchViewModel(androidApplication(), get()) }
        viewModel { FragmentTopAlbumsViewModel(androidApplication(), get()) }
        viewModel { FragmentAlbumDetailsViewModel(androidApplication(), get()) }
//        viewModel { FragmentNewEditListViewModel(get(), androidApplication()) }
//        viewModel { FragmentListItemsViewModel(androidApplication()) }
//        viewModel { FragmentNewListItemsViewModel(androidApplication()) }
//        viewModel { FragmentEditListItemViewModel(androidApplication()) }
//        viewModel { ActivityLoginViewModel(androidApplication()) }
//        viewModel { ActivityNavGraphViewModel(get(), get(), androidApplication()) }
//        factory<RepositoryUserLists> { RepositoryUserListsImpl() }
//        factory<RepositoryListItems> { (listId: UUID) -> RepositoryListItemsImpl(listId) }
        single<RepositoryArtists> { RepositoryArtistsImpl(SMyRetrofit.lastFmApi) }
        single<RepositoryAlbums> { RepositoryAlbumsImpl(SMyRetrofit.lastFmApi) }
        single { SMyDatabase.getInstance(androidApplication()) }
        single { get<SMyDatabase>().daoAlbums() }
    }

//todo delete this when ready
// if not deleting, make sure that all dependencies in the appModule are included here
//        val testModule = module(override = true) {
//            factory<RepositoryUserLists> { RepositoryUserListsImplFake() }
//            factory<RepositoryListItems> { (listId: UUID) -> RepositoryListItemsImplFake(listId) }
//        }

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