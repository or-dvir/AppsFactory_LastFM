package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.RowAlbumBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size
import com.hotmail.or_dvir.appsfactory_lastfm.other.repositories.RepositoryAlbums
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class AdapterAlbums(
    val items: MutableList<Album>,
    val onFavoriteClick: (Int, Album) -> Unit
) : DxAdapter<Album, AdapterAlbums.ViewHolder>(), KoinComponent
{
    //todo some code is duplicated from artist adapter. can i create a shared base adapter?

    val repoAlbums: RepositoryAlbums = get()

    override fun getDxAdapterItems() = items

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
            val item = items[holder.bindingAdapterPosition]
            val placeholderImageRes = R.drawable.ic_album_placeholder

            tvAlbumName.text = item.name

            //todo BAD! either fix this later, or add a note in the documentation
            // saying you know its bad!
            // 1. dont start an "unbound" coroutine scope like that.
            // 2. dont perform db queries in this function! it will be called many times!!!
            CoroutineScope(Dispatchers.IO).launch {
                ivFavorite.apply {
                    val isInFavorites =
                        item.canBeStoredInDb() && repoAlbums.isInFavorites(item.dbUUID!!)

                    val favoriteRes = when
                    {
                        !item.canBeStoredInDb() -> R.drawable.ic_favorite_broken
                        isInFavorites -> R.drawable.ic_favorite_filled
                        //item can be stored and is NOT in favorites
                        else -> R.drawable.ic_favorite_outline
                    }

                    withContext(Dispatchers.Main) {
                        setImageResource(favoriteRes)
                    }
                }
            }

            ivAlbumImage.apply {
                val imageUrl = item.getImageUrl(Size.LARGE)
                val picassoRequest =
                    if (imageUrl.isNullOrBlank())
                    {
                        Picasso.get().load(placeholderImageRes)
                    } else
                    {
                        //we have a valid url
                        Picasso.get().load(imageUrl)
                    }

                picassoRequest
                    .error(placeholderImageRes)
                    .into(this)
            }
        }
    }

    override fun onViewRecycled(holder: ViewHolder)
    {
        holder.binding.apply {
            tvAlbumName.text = ""
            //todo some of the images dont show.
            // it might be fixed when setting image in onBindViewHolder()
//            ivFavorite.setImageDrawable(null)
            Picasso.get().cancelRequest(ivAlbumImage)
        }

        super.onViewRecycled(holder)
    }

    fun setData(newData: List<Album>)
    {
        items.apply {
            clear()
            addAll(newData)
        }
        //note that there is no need to notify the adapter because we are using DiffUtil
    }

    fun isEmpty() = itemCount == 0

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
