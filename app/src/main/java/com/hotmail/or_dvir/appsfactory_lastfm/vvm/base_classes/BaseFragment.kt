package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.hotmail.or_dvir.appsfactory_lastfm.R
import or_dvir.hotmail.com.dxutils.hideKeyBoard
import or_dvir.hotmail.com.dxutils.makeVisibleOrGone

abstract class BaseFragment() : Fragment()
{
    //region abstract
    abstract fun getLoadingView(): View?
    abstract fun getViewModel(): BaseAndroidViewModel
    //endregion

    private lateinit var observerLoading: Observer<Boolean>

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        //automatically add menu options. if no menu exists, it will simply not be inflated.
        setHasOptionsMenu(true)
        initializeObservers()
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        //this intercepts touch events so the user cannot interact with the screen
        //while the loading view is showing
        getLoadingView()?.setOnTouchListener { _, _ -> true }
        getViewModel().isLoading.observe(viewLifecycleOwner, observerLoading)
    }

    override fun onPause()
    {
        view?.let { hideKeyBoard(it, 0) }
        super.onPause()
    }

    private fun initializeObservers()
    {
        observerLoading = Observer {
            getLoadingView()?.makeVisibleOrGone(it)
        }
    }

    fun getColor(@ColorRes color: Int) =
        ContextCompat.getColor(requireContext(), color)
}