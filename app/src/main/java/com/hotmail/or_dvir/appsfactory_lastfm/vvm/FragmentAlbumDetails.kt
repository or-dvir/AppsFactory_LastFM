package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentAlbumDetailsBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.snackbar
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseFragment
import or_dvir.hotmail.com.dxutils.makeGone
import or_dvir.hotmail.com.dxutils.makeVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentAlbumDetails : BaseFragment()
{
    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!
    private val fragArgs: FragmentAlbumDetailsArgs by navArgs()

    @VisibleForTesting
    internal val viewModel: FragmentAlbumDetailsViewModel by viewModel()
    private lateinit var observerAlbum: Observer<Album>

    override fun getLoadingView() = binding.loadingView.parent
    override fun getViewModel() = viewModel

    //no recycler view for this fragment
    override fun getRecyclerView(): RecyclerView? = null

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
        _binding = FragmentAlbumDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
        viewModel.apply {
            getAlbumDetails(fragArgs.artistName, fragArgs.albumName)
            album.observe(viewLifecycleOwner, observerAlbum)
        }
    }

    private fun initializeObservers()
    {
        observerAlbum = Observer {
            binding.apply {
                if (it == null)
                {

                    view?.snackbar(R.string.error_general)
                    svAlbumDetailsContainer.makeGone()
                    return@Observer
                }

                tvAlbumNameAndArtist.text = getString(
                    R.string.title_s_by_s,
                    //todo should i use the info from the object, or from the arguments
                    // of the fragment?
                    it.artist.name,
                    fragArgs.artistName
                )

                tvTracks.text = viewModel.getAlbumTracksAsListText()
                    ?: getString(R.string.error_noTrackInformationAvailable)

                svAlbumDetailsContainer.makeVisible()
            }
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()
        _binding = null
    }
}