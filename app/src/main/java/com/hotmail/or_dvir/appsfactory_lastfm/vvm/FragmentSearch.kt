package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup

import android.widget.EditText
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentSearchBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.other.longSnackbar
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSearch : BaseFragment()
{
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    //todo
    // add recycler view with results of search
    // add empty view

    @VisibleForTesting
    internal val viewModel: FragmentSearchViewModel by viewModel()
    private lateinit var observerArtistsSearch: Observer<List<Artist>>

    override fun getLoadingView() = binding.loadingView.parent
    override fun getViewModel() = viewModel

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        initializeObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel.artists.observe(viewLifecycleOwner, observerArtistsSearch)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_search, menu)
        val searchView = menu.findItem(R.id.menuItem_search).actionView as SearchView
        setupSearchView(searchView)

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupSearchView(sv: SearchView)
    {
        sv.apply {
            queryHint = getString(R.string.hint_artistName)
            isSubmitButtonEnabled = true
            findViewById<EditText>(androidx.appcompat.R.id.search_src_text).apply {
                val textColor = ContextCompat.getColor(context, R.color.white)
                setTextColor(textColor)
                setHintTextColor(textColor)
            }

            setOnQueryTextListener(
                object : SearchView.OnQueryTextListener
                {
                    override fun onQueryTextSubmit(query: String?): Boolean
                    {
                        query?.let { viewModel.searchArtists(it) }
                        return true
                    }

                    override fun onQueryTextChange(newText: String?): Boolean
                    {
                        //do nothing (but use default search view behaviour).
                        return false
                    }
                }
            )
        }
    }

    private fun initializeObservers()
    {
        observerArtistsSearch = Observer {
            if (it == null)
            {
                view?.longSnackbar(R.string.error_general)
                return@Observer
            }

            //todo add results to the recycler view
            // use DiffUtil
        }
    }


    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}