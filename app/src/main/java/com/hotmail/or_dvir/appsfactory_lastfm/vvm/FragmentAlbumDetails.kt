package com.hotmail.or_dvir.appsfactory_lastfm.vvm

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.FragmentAlbumDetailsBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.loadWithPicasso
import com.hotmail.or_dvir.appsfactory_lastfm.other.snackbar
import com.hotmail.or_dvir.appsfactory_lastfm.vvm.base_classes.BaseFragment
import kotlinx.coroutines.launch
import or_dvir.hotmail.com.dxutils.makeGone
import or_dvir.hotmail.com.dxutils.makeVisible
import org.koin.androidx.viewmodel.ext.android.viewModel

class FragmentAlbumDetails : BaseFragment()
{
    private var _binding: FragmentAlbumDetailsBinding? = null
    private val binding get() = _binding!!
    private val fragArgs: FragmentAlbumDetailsArgs by navArgs()

    private val viewModel: FragmentAlbumDetailsViewModel by viewModel()
    private lateinit var observerAlbum: Observer<Album?>

    override fun getLoadingView() = binding.loadingView.parent
    override fun getFragViewModel() = viewModel

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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater)
    {
        inflater.inflate(R.menu.menu_fragment_album_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu)
    {
        //uses main dispatcher by default
        lifecycleScope.launch {
            val album = viewModel.album.value
            val favoritesMenuItem = menu.findItem(R.id.menuItem_albumDetails_addOrRemoveFavorite)

            favoritesMenuItem.apply {
                if (album == null || !album.canBeStoredInDb())
                {
                    isVisible = false
                } else
                {
                    //album is NOT null AND has a valid dbUUID
                    isVisible = true
                    //setting the tint in the menu xml is not enough.
                    iconTintList = ColorStateList.valueOf(getColor(R.color.white))

                    if (viewModel.isInFavorites())
                    {
                        setTitle(R.string.removeFromFavorites)
                        setIcon(R.drawable.ic_favorite_filled)
                    } else
                    {
                        setTitle(R.string.addToFavorites)
                        setIcon(R.drawable.ic_favorite_outline)
                    }
                }
            }

            super.onPrepareOptionsMenu(menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == R.id.menuItem_albumDetails_addOrRemoveFavorite)
        {
            viewModel.addOrRemoveAlbumFavorites { error ->
                error?.let { view?.snackbar(it) }
                //favorites status changed - reset menu
                    ?: activity?.invalidateOptionsMenu()
            }
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initializeObservers()
    {
        observerAlbum = Observer {
            binding.apply {
                //we got a new album - reset menu
                activity?.invalidateOptionsMenu()

                if (it == null)
                {
                    view?.snackbar(R.string.error_general)
                    svAlbumDetailsContainer.makeGone()
                    return@Observer
                }

                tvAlbumNameAndArtist.apply {
                    //album name is null - "album by <artist>"
                    //artist is null - <album name>
                    //both null - nothing

                    val isArtistNameValid = it.isArtistNameValid()
                    val isAlbumNameValid = it.isNameValid()

                    text = when
                    {
                        isAlbumNameValid && isArtistNameValid ->
                            getString(R.string.title_s_by_s, it.name, it.artist!!.name)
                        //only album name is valid
                        isAlbumNameValid -> "\"${it.name}\""
                        //only artist name is valid
                        else -> getString(R.string.title_album_by_s, it.artist!!.name)
                    }
                }

                ivAlbumImage.loadWithPicasso(it.getImageUrl(), R.drawable.ic_album_placeholder)

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