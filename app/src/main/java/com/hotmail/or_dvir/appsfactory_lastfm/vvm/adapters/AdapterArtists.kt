package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.RowArtistBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.squareup.picasso.Picasso

class AdapterArtists(items: MutableList<Artist>) :
    BaseAdapter<Artist, AdapterArtists.ViewHolder>(items)
{
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

            val imageUrl = item.getImageUrl()
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

    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////
    /////////////////////////////////////////

    class ViewHolder(val binding: RowArtistBinding) : RecyclerView.ViewHolder(binding.root)
}
