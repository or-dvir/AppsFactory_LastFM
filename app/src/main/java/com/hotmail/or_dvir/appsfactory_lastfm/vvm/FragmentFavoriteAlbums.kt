package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
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

    i stopped here! fix all todo notes!!!

    //todo
    // FIX NAVIGATION GRAPH!!!
    // clicking an album opens the details page
    // add button to go to search fragment
    // show loading dialog when loading albums
    //      how? its observed directly from the database!
    //      perhaps automatically show loading dialog, and hide it in observer...

    private var _binding: FragmentFavoriteAlbumsBinding? = null
    private val binding get() = _binding!!

    @VisibleForTesting
    internal val viewModel: FragmentFavoriteAlbumsViewModel by viewModel()

    private lateinit var observerAlbums: Observer<List<Album>>

    //todo using same adapter as top albums because it has almost the exact same
    // functionality as we need here. the only difference (for now) is that
    // we know for sure that all the albums in this fragment ARE favorite albums
    // so some calculations will be performed even though they are not needed
    // for this fragment
    private lateinit var rvAdapter: AdapterAlbums


    override fun getLoadingView() = binding.loadingView.parent
    override fun getRecyclerView() = binding.rv
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
        _binding = FragmentFavoriteAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //initialize with empty list
        rvAdapter = AdapterAlbums(mutableListOf()) { position, album ->
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
                        //todo navigate to details fragment
//                        findNavController().navigate(
//                            FragmentTopAlbumsDirections.actionFragmentTopAlbumsToFragmentAlbumDetails(
//                                getArtistName(),
//                                item.name
//                            )
//                        )
                    },
                    onItemLongClick = { _, _, _ ->
                        //do nothing
                        false
                    }
                )
            )
        }

        binding.apply {
            rv.apply {
                adapter = rvAdapter

                //todo dynamically calculate number of columns?
                // if not, add note that this is based on your own device and in production
                // will be calculated dynamically
                layoutManager = GridLayoutManager(context, 3)
            }
        }

        viewModel.favoriteAlbums.observe(viewLifecycleOwner, observerAlbums)
    }

    private fun initializeObservers()
    {
        observerAlbums = Observer { newList ->
            if (newList == null)
            {
                binding.apply {
                    rv.makeGone()
                    //todo make better error
                    view?.snackbar(R.string.error_general)
                }

                return@Observer
            }

            val exceptionHandler = CoroutineExceptionHandler { _, t ->
                Log.e(TAG, t.message, t)
                //todo make better error
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
                        //todo show/hide empty view
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