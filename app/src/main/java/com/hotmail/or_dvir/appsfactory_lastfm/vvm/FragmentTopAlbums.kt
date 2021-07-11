package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentTopAlbumsBinding
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

class FragmentTopAlbums : BaseFragment()
{
    private companion object
    {
        private const val TAG = "FragmentTopAlbums"
    }

    //todo
    // when showing an error for adding/removing album, if many albums have errors,
    //      the snackbar will quickly disappear.
    // check api!!!! album object does not include list of tracks!!! (need to store this)

    private var _binding: FragmentTopAlbumsBinding? = null
    private val binding get() = _binding!!
    private val fragArgs: FragmentTopAlbumsArgs by navArgs()

    @VisibleForTesting
    internal val viewModel: FragmentTopAlbumsViewModel by viewModel()
    private lateinit var observerAlbums: Observer<List<Album>?>
    private lateinit var rvAdapter: AdapterAlbums

    override fun getLoadingView() = binding.loadingView.parent
    override fun getViewModel() = viewModel
    override fun getRecyclerView() = binding.rv

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
        _binding = FragmentTopAlbumsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        //initialize with empty list
        rvAdapter = AdapterAlbums(mutableListOf()) { position, album ->
            //favorite icon click listener
            viewModel.addOrRemoveAlbum(album) { error ->
                error?.let { getView()?.snackbar(it) }
                    ?: rvAdapter.notifyItemChanged(position)
            }

            //todo add note about using listener and not diff util:
            // there is no indication in the Album class as to whether
            // it is in favorites or not, so the diffcallback will think nothing has changed
        }.apply {
            addFeature(
                DxFeatureClick<Album>(
                    onItemClick = { _, _, item ->
                        findNavController().navigate(
                            FragmentTopAlbumsDirections.actionFragmentTopAlbumsToFragmentAlbumDetails(
                                getArtistName(),
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

        binding.apply {
            viewModel.loadTopAlbums(getArtistName())

            rv.apply {
                //todo might be needed for pagination?
//            onScrollListener = DxScrollListener(25).apply {
//                onScrollDown = { getFab().hide() }
//                onScrollUp = { getFab().show() }
//            }

                adapter = rvAdapter
                layoutManager =
                    GridLayoutManager(context, getInteger(R.integer.spanCountForGridLayout))
            }
        }

        viewModel.topAlbums.observe(viewLifecycleOwner, observerAlbums)
    }

    private fun initializeObservers()
    {
        observerAlbums = Observer { newList ->
            if (newList == null)
            {
                binding.apply {
                    rv.makeGone()
                    //todo make better error
                    tvTitle.text = getString(R.string.error_general)
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
                        val titleRes =
                            if (isEmpty())
                            {
                                R.string.error_noResults_s
                            } else
                            {
                                R.string.title_topAlbumsFor_s
                            }

                        tvTitle.text = getString(titleRes, getArtistName())
                        rv.makeVisibleOrGone(!isEmpty())
                    }
                }
            }
        }
    }

    private fun getArtistName() = fragArgs.artistName

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}