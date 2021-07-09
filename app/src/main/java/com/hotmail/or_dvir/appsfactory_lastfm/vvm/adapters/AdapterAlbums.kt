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

class AdapterAlbums(
    val items: MutableList<Album>
) : DxAdapter<Album, AdapterAlbums.ViewHolder>()
{
    //todo some code is duplicated from artist adapter. can i create a shared base adapter?

    private var favoriteAlbums = listOf<Album>()

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
                val favoriteRes = when
                {
                    !item.canBeStoredInDb() -> R.drawable.ic_favorite_broken
                    isInFavorites(item) -> R.drawable.ic_favorite_filled
                    //item can be stored and is NOT in favorites
                    else -> R.drawable.ic_favorite_outline
                }

                setImageResource(favoriteRes)
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

    //we do not use the contains() function because that is based on Album.equals().
    //since Album is a data class, its' equals() function consists of all the fields in the
    //primary constructor, whereas 2 albums might be the same even with slightly varied fields
    //(e.g. from database and from lastFM API, since they don't exactly match).
    //it's easier to have this function than to override the Album.equals() method.
    private fun isInFavorites(album: Album) =
        favoriteAlbums.find { it.dbUUID == album.dbUUID } != null

    fun setData(newData: List<Album>, favoriteAlbums: List<Album>)
    {
        this.favoriteAlbums = favoriteAlbums

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
        val binding: RowAlbumBinding
    ) : RecyclerView.ViewHolder(binding.root)
    {
        init
        {
            proposed solution:
                    have 2 interfaces (or lambdas). one for view holder (only with position)
                    and one for adapter (with position and item)
            then propagae the click from the view holder (only with position)
            to the adapter inside getAdapterViewHolder (where you can retrieve the item),
            to the fragment (who will receive both position and item)

                    see here https://oozou.com/blog/a-better-way-to-handle-click-action-in-a-recyclerview-item-60
        }
    }
}
