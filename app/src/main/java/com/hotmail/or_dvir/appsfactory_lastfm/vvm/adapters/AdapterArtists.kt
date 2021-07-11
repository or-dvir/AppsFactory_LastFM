package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.RowArtistBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size
import com.hotmail.or_dvir.dxadapter.DxAdapter
import com.squareup.picasso.Picasso

class AdapterArtists(val items: MutableList<Artist>) :
    DxAdapter<Artist, AdapterArtists.ViewHolder>()
{
    override fun getDxAdapterItems() = items

    override fun createAdapterViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder
    {
        val binding = RowArtistBinding.inflate(
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
            val placeholderImageRes = R.drawable.ic_artist_placeholder

            tvArtistName.text = item.name

            val imageUrl = item.getImageUrl(Size.MEDIUM)
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
                .into(ivArtistImage)
        }
    }

    override fun onViewRecycled(holder: ViewHolder)
    {
        holder.binding.apply {
            tvArtistName.text = ""
            Picasso.get().cancelRequest(ivArtistImage)
        }

        super.onViewRecycled(holder)
    }

    fun setData(data: List<Artist>)
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

    class ViewHolder(val binding: RowArtistBinding) : RecyclerView.ViewHolder(binding.root)
}
