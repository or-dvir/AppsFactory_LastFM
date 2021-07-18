package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.app.Application
import android.support.annotation.StringRes
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.core.component.KoinComponent

abstract class BaseAndroidViewModel(val app: Application) : AndroidViewModel(app), KoinComponent
{
    val isLoading = MutableLiveData<Boolean>()

    fun getString(@StringRes stringRes: Int) = app.getString(stringRes)

    fun createCoroutineExceptionHandler(logTag: String, onException: (() -> Unit)? = null) =
        CoroutineExceptionHandler { _, t ->
            Log.e(logTag, t.message, t)
            isLoading.value = false
            onException?.invoke()
        }
}