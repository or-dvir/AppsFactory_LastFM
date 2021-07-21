package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.app.Application
import android.support.annotation.StringRes
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.component.KoinComponent

/**
 * a base class holding some shared functionality for all of the view models in this app
 */
abstract class BaseAndroidViewModel(val app: Application) : AndroidViewModel(app), KoinComponent
{
    /**
     * represents whether or not this [BaseAndroidViewModel] is currently performing
     * a long running operation
     */
    val isLoading = MutableLiveData<Boolean>()

    /**
     * a helper function to retrieve a string with the given [stringRes]
     */
    fun getString(@StringRes stringRes: Int) = app.getString(stringRes)

    /**
     * returns a new [CoroutineExceptionHandler]
     *
     * @param logTag String the tag to be used when logging the exception
     * @param onException (optional) a listener to be invoked when this [CoroutineExceptionHandler]
     * is triggered
     */
    fun createCoroutineExceptionHandler(logTag: String, onException: (() -> Unit)? = null) =
        CoroutineExceptionHandler { _, t ->
            Log.e(logTag, t.message, t)
            isLoading.value = false
            onException?.invoke()
        }
}