package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.VisibleForTesting
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentSearchBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.other.longSnackbar
import com.hotmail.or_dvir.appsfactory_lastfm.other.snackbar
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters.AdapterArtists
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseFragment
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import or_dvir.hotmail.com.dxutils.hideKeyBoard
import or_dvir.hotmail.com.dxutils.makeGone
import or_dvir.hotmail.com.dxutils.makeVisibleOrGone
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentTopAlbums : BaseFragment()
{
    private companion object
    {
        private const val TAG = "FragmentTopAlbums"
    }

    //todo
    // when showing an error for adding/removing album, if many albums have errors,
    //      the snackbar will quickly disappear.
    // observe albums list
    // observe albums error
    // check api!!!! album object does not include list of tracks!!!

    private var _binding: FragmentTopAlbums? = null
    private val binding get() = _binding!!

    @VisibleForTesting
    internal val viewModel: FragmentTopAlbumsViewModel by viewModel()
    private lateinit var observerArtistsSearch: Observer<List<Artist>>
    private lateinit var rvAdapter: AdapterArtists

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

        //initialize with empty list
        rvAdapter = AdapterArtists(mutableListOf()).apply {
            addFeature(
                DxFeatureClick<Artist>(
                    onItemClick = { view, adapterPosition, item ->
                        //todo do me!!!
                        getView()?.snackbar("click")
                    },
                    onItemLongClick = { view, adapterPosition, item ->
                        //do nothing
                        false
                    }
                )
            )
        }

        binding.rv.apply {
            //todo might be needed for pagination?
//            onScrollListener = DxScrollListener(25).apply {
//                onScrollDown = { getFab().hide() }
//                onScrollUp = { getFab().show() }
//            }

            adapter = rvAdapter

            layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

            addItemDecoration(
                DividerItemDecoration(
                    context, (layoutManager as LinearLayoutManager).orientation
                )
            )
        }

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
                setTextColor(getColor(R.color.white))
                setHintTextColor(getColor(R.color.whiteTransparent))
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
        observerArtistsSearch = Observer { newList ->
            view?.let { hideKeyBoard(it, InputMethodManager.HIDE_NOT_ALWAYS) }

            if (newList == null)
            {
                binding.apply {
                    rv.makeGone()
                    emptyView.makeGone()
                }

                //todo make better error
                view?.longSnackbar(R.string.error_general)
                return@Observer
            }

            val exceptionHandler = CoroutineExceptionHandler { _, t ->
                Log.e(TAG, t.message, t)
                //todo make better error
                view?.longSnackbar(R.string.error_general)
            }

            //ui operation - should by tied to lifecycle scope.
            //uses main dispatcher by default.
            lifecycleScope.launch(exceptionHandler) {
                //switch to Dispatchers.default which is recommended for DiffUtil
                val result = withContext(Dispatchers.Default) {
                    DiffUtil.calculateDiff(
                        Artist.DiffCallback(rvAdapter.items, newList),
                        true
                    )
                }

                //back to using main dispatcher
                rvAdapter.apply {
                    setData(newList)
                    result.dispatchUpdatesTo(this)

                    //handle views visibility
                    binding.apply {
                        //if no results were found, and the user has performed at least one
                        // search before, show an appropriate error message.
                        //(this observer will be called with an empty list the first time the
                        // fragment is created)
                        if (isEmpty() && viewModel.hasSearchedBefore)
                        {
                            emptyView.text =
                                String.format(
                                    getString(R.string.error_artistSearchResults_s),
                                    viewModel.lastSearchQuery
                                )
                        }

                        rv.makeVisibleOrGone(!isEmpty())
                        emptyView.makeVisibleOrGone(isEmpty())
                        rv.scrollToPosition(0)
                    }
                }
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}