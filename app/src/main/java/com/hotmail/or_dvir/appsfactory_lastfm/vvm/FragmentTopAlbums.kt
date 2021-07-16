package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentTopAlbumsBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.hasInternetConnection
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

    private var _binding: FragmentTopAlbumsBinding? = null
    private val binding get() = _binding!!
    private val fragArgs: FragmentTopAlbumsArgs by navArgs()
    private val viewModel: FragmentTopAlbumsViewModel by viewModel()
    private lateinit var observerAlbums: Observer<List<Album>?>
    private lateinit var rvAdapter: AdapterAlbums

    override fun getLoadingView() = binding.loadingView.parent
    override fun getFragViewModel() = viewModel
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
                //note: using listener and not diff util because there is no indication
                // in the Album class as to whether it is in favorites or not,
                // so the diffCallback will think nothing has changed
                error?.let { getView()?.snackbar(it) }
                    ?: rvAdapter.notifyItemChanged(position)
            }
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
            if (requireContext().hasInternetConnection())
            {
                viewModel.loadTopAlbums(getArtistName())
            } else
            {
                setError(getString(R.string.error_offline))
            }

            rv.apply {
                adapter = rvAdapter
                layoutManager =
                    GridLayoutManager(context, getInteger(R.integer.spanCountForGridLayout))
            }
        }

        viewModel.topAlbums.observe(viewLifecycleOwner, observerAlbums)
    }

    private fun setError(error: String)
    {
        binding.apply {
            rv.makeGone()
            tvTitle.text = error
        }
    }

    private fun initializeObservers()
    {
        observerAlbums = Observer { newList ->
            if (newList == null)
            {
                //todo make better error
                setError(getString(R.string.error_general))
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