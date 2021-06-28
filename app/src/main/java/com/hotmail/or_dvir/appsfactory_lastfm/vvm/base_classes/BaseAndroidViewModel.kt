package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.koin.core.component.KoinComponent

abstract class BaseAndroidViewModel(app: Application) : AndroidViewModel(app), KoinComponent
{
    val isLoading = MutableLiveData<Boolean>()
}