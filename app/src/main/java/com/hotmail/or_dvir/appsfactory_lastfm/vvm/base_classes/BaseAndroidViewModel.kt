package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.app.Application
import android.support.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.koin.core.component.KoinComponent

abstract class BaseAndroidViewModel(val app: Application) : AndroidViewModel(app), KoinComponent
{
    //todo a lot of pretty much duplicated exception handlers for coroutines
    // can i make them shared?

    val isLoading = MutableLiveData<Boolean>()

    fun getString(@StringRes stringRes: Int) = app.getString(stringRes)

    fun getString(@StringRes stringRes: Int, vararg args: String) =
        String.format(app.getString(stringRes), args)
}