package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.RowAlbumBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.squareup.picasso.Picasso

class AdapterAlbums(val items: MutableList<Album>) :
    DxAdapter<Album, AdapterAlbums.ViewHolder>()
{
    //todo some code is duplicated from artist adapter. can i create a shared base adapter?

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

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int)
    {
        super.onBindViewHolder(holder, position)

        holder.binding.apply {
            val item = items[holder.bindingAdapterPosition]
            val placeholderImageRes = R.drawable.ic_album_placeholder

            tvAlbumName.text = item.name

            ivFavorite.apply {
                if (!item.canBeStoredInDb())
                {
                    setImageResource(R.drawable.ic_favorite_broken)
                }

                //todo set icon for albums according to whether they are already
                // in favorites or not
                //todo handle clicking favorite button
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

                //todo do i need scaling?
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

    fun setData(data: List<Album>)
    {
        items.apply {
            clear()
            addAll(data)
        }
        //note that there is no need to notify the adapter because we are using DiffUtil
    }

    fun isEmpty() = itemCount == 0

    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////

    class ViewHolder(val binding: RowAlbumBinding) : RecyclerView.ViewHolder(binding.root)
}
