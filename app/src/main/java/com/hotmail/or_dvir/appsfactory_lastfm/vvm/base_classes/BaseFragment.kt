package com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.ColorRes
import androidx.annotation.IntegerRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import or_dvir.hotmail.com.dxutils.hideKeyBoard
import or_dvir.hotmail.com.dxutils.makeVisibleOrGone

/**
 * a base class holding some shared functionality for all the fragments in this app
 */
abstract class BaseFragment : Fragment()
{
    //region abstract
    /**
     * returns a view used to indicate the app is loading some data,
     * or null if no such view exists for this fragment
     */
    abstract fun getLoadingView(): View?

    /**
     * returns this framgnets' view model
     */
    abstract fun getFragViewModel(): BaseAndroidViewModel

    /**
     * returns this fragments [RecyclerView], or null if this fragment does not
     * contain one
     */
    abstract fun getRecyclerView(): RecyclerView?
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
        getFragViewModel().isLoading.observe(viewLifecycleOwner, observerLoading)
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

    /**
     * a helper function to retrieve the integer representation of the given [colorRes]
     */
    fun getColor(@ColorRes colorRes: Int) =
        ContextCompat.getColor(requireContext(), colorRes)

    /**
     * a helper function to retrieve the integer value represented by the given [IntegerRes]
     */
    fun getInteger(@IntegerRes intRes: Int) = resources.getInteger(intRes)

    override fun onDestroyView()
    {
        //prevents memory leak
        getRecyclerView()?.adapter = null
        super.onDestroyView()
    }
}