package com.hotmail.or_dvir.appsfactory_lastfm.vvm.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.databinding.RowArtistBinding
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.other.loadWithPicasso
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
            val artist = items[holder.bindingAdapterPosition]
            tvArtistName.text = artist.name
            ivArtistImage.loadWithPicasso(artist.getImageUrl(), R.drawable.ic_artist_placeholder)
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
