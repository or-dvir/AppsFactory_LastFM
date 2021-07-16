package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentFavoriteAlbumsBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.longSnackbar
import com.hotmail.or_dvir.appsfactory_lastfm.other.snackbar
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters.AdapterAlbums
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseFragment
import com.hotmail.or_dvir.dxclick.DxFeatureClick
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import or_dvir.hotmail.com.dxutils.makeGone
import or_dvir.hotmail.com.dxutils.makeVisibleOrGone
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentFavoriteAlbums : BaseFragment()
{
    private companion object
    {
        private const val TAG = "FragmentFavoriteAlbums"
    }

    private var _binding: FragmentFavoriteAlbumsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FragmentFavoriteAlbumsViewModel by viewModel()
    private lateinit var observerAlbums: Observer<List<Album>>

    //note: we are using same adapter as top albums because it has the exact same
    // functionality as we need here. the only difference is querying whether each entry is
    // favorite or not, which is always true in this fragment.
    // so this query will be performed even though it is not needed here.
    // for the purposes of this demo app, it is good enough.
    private lateinit var rvAdapter: AdapterAlbums


    override fun getLoadingView() = binding.loadingView.parent
    override fun getRecyclerView() = binding.rv
    override fun getFragViewModel() = viewModel

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
        _binding = FragmentFavoriteAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //initialize with empty list
        rvAdapter = AdapterAlbums(mutableListOf()) { _, album ->
            //favorite icon click listener
            viewModel.removeAlbum(album) { success ->
                if (!success)
                {
                    getView()?.snackbar(R.string.error_removingAlbum)
                }
            }
        }.apply {
            addFeature(
                DxFeatureClick<Album>(
                    onItemClick = { _, _, item ->
                        findNavController().navigate(
                            FragmentFavoriteAlbumsDirections.actionFragmentFavoritesToFragmentAlbumDetails(
                                item.artist?.name,
                                item.name
                            )
                        )
                    },
                    onItemLongClick = { _, _, _ ->
                        //do nothing
                        false
                    }
                )
            )
        }

        binding.rv.apply {
            adapter = rvAdapter
            layoutManager =
                GridLayoutManager(context, getInteger(R.integer.spanCountForGridLayout))
        }

        viewModel.favoriteAlbums.observe(viewLifecycleOwner, observerAlbums)
    }

    private fun initializeObservers()
    {
        observerAlbums = Observer { newList ->
            viewModel.isLoading.value = false

            if (newList == null)
            {
                binding.apply {
                    rv.makeGone()
                    view?.snackbar(R.string.error_general)
                }

                return@Observer
            }

            val exceptionHandler = CoroutineExceptionHandler { _, t ->
                Log.e(TAG, t.message, t)
                view?.longSnackbar(R.string.error_general)
            }

            //todo copied from search fragment. can i make this shared?
            //ui operation - should by tied to lifecycle scope.
            //uses main dispatcher by default.
            lifecycleScope.launch(exceptionHandler) {
                //switch to Dispatchers.default which is recommended for DiffUtil
                val result = withContext(Dispatchers.Default) {
                    DiffUtil.calculateDiff(
                        Album.DiffCallback(rvAdapter.items, newList),
                        false
                    )
                }

                //back to using main dispatcher
                rvAdapter.apply {
                    setData(newList)
                    result.dispatchUpdatesTo(this)

                    //handle views visibility
                    binding.apply {
                        rv.makeVisibleOrGone(!isEmpty())
                        tvEmptyView.makeVisibleOrGone(isEmpty())
                    }
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_fragment_favorites, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.menuItem_favorites_search)
        {
            findNavController().navigate(
                FragmentFavoriteAlbumsDirections.actionFragmentFavoritesToFragmentSearch()
            )
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}