package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.model.Image.Companion.Size
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "mbid")
    val id: String,
    @Json(name = "name")
    val name: String,
    //when searching for albums, they include an Artist field without images,
    //so we give it a default value for those cases
    @Json(name = "image")
    val images: List<Image> = listOf()
) : IDxItemClickable
{
    fun getImageUrl(size: Size) = images.find {
        //blank urls are not allowed in Picasso
        it.size == size //&& it.url.isNotBlank()
    }?.url

    override fun getViewType() = R.id.viewType_Artist

    //todo some artists dont have ids!!! e.g. "Alice Cooper feat. Roger Glover"
    // consider this when calculating DiffUtil!!!
    // is it possible for an album not to have an id???

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    class DiffCallback(private val oldList: List<Artist>, private val newList: List<Artist>) :
        DiffUtil.Callback()
    {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            //todo should i add images here too?
            return old.name == new.name
        }

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    }
}