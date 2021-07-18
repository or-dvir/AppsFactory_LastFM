package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.RowAlbumBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.loadWithPicasso
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AdapterAlbums(
    items: MutableList<Album>,
    val onFavoriteClick: (Int, Album) -> Unit
) : BaseAdapter<Album, AdapterAlbums.ViewHolder>(items), KoinComponent
{
    private val repoAlbums: RepositoryAlbums = get()

    override fun createAdapterViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder
    {
        val binding = RowAlbumBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ViewHolder(binding) {
            onFavoriteClick(it, items[it])
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        super.onBindViewHolder(holder, position)

        holder.binding.apply {
            val album = items[holder.bindingAdapterPosition]

            tvAlbumName.apply {
                text = if (album.isNameValid())
                {
                    album.name
                } else
                {
                    context.getString(R.string.unknownAlbum)
                }
            }

            //BAD PROGRAMMER! BAD!
            // - its bad to start a coroutine scope like that.
            // - its bad to perform db queries in this function, since it will be called many times
            //      and this is very inefficient!
            //of course in a real app, we would NEVER use this "technique".
            //however... if we want to attach a "favorites" button for every album in this adapter,
            //since we are using DiffUtil, we need to either include (and properly maintain it)
            //a field "isFavorite" in the Album class, or create a mechanism where this adapter
            //knows about all the users' favorite albums (and properly maintain it).
            //due to time constraints, such a mechanism would be too complicated to implement.
            CoroutineScope(Dispatchers.IO).launch {
                ivFavorite.apply {
                    val isInFavorites =
                        album.canBeStoredInDb() && repoAlbums.isInFavorites(album.dbUUID!!)

                    val favoriteRes = when
                    {
                        !album.canBeStoredInDb() -> R.drawable.ic_favorite_broken
                        isInFavorites -> R.drawable.ic_favorite_filled
                        //item can be stored and is NOT in favorites
                        else -> R.drawable.ic_favorite_outline
                    }

                    withContext(Dispatchers.Main) {
                        setImageResource(favoriteRes)
                    }
                }
            }

            ivAlbumImage.loadWithPicasso(album.getImageUrl(), R.drawable.ic_album_placeholder)
        }
    }

    override fun onViewRecycled(holder: ViewHolder)
    {
        Picasso.get().cancelRequest(holder.binding.ivAlbumImage)
        super.onViewRecycled(holder)
    }

    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////

    class ViewHolder(
        val binding: RowAlbumBinding,
        val onFavoriteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root)
    {
        init
        {
            binding.ivFavorite.setOnClickListener { onFavoriteClick(bindingAdapterPosition) }
        }
    }
}
