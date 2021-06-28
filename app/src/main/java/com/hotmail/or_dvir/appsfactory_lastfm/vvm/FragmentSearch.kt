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
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentSearchBinding
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentSearch : BaseFragment()
{
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    @VisibleForTesting
    internal val viewModel: FragmentSearchViewModel by viewModel()

    override fun getLoadingView() = binding.loadingView.parent
    override fun getViewModel() = viewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View
    {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
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

            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean
                {
                    //todo perform actual search
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean
                {
                    //do nothing (but use default search view behaviour).
                    return false
                }
            })

        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}