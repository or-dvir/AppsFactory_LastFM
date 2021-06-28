package com.hotmail.or_dvir.appsfactory_lastfm.other

import android.app.Application
import android.util.Log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MyApplication : Application()
{
    companion object
    {
        private const val TAG = "MyApplication"
    }

    //todo keeo for reference. delete if unused
    val appModule = module {
//        viewModel { FragmentUserListsViewModel(get(), androidApplication()) }
//        viewModel { FragmentNewEditListViewModel(get(), androidApplication()) }
//        viewModel { FragmentListItemsViewModel(androidApplication()) }
//        viewModel { FragmentNewListItemsViewModel(androidApplication()) }
//        viewModel { FragmentEditListItemViewModel(androidApplication()) }
//        viewModel { ActivityLoginViewModel(androidApplication()) }
//        viewModel { ActivityNavGraphViewModel(get(), get(), androidApplication()) }
//        factory<RepositoryUserLists> { RepositoryUserListsImpl() }
//        factory<RepositoryListItems> { (listId: UUID) -> RepositoryListItemsImpl(listId) }
//        single<RepositoryOldLists> { RepositoryOldListsImpl(androidApplication(), Dispatchers.IO) }
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