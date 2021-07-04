package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "mbid")
    val id: String,
    @Json(name = "name")
    val name: String,
    @Json(name = "image")
    val images: List<Image> = listOf()
) : IDxItemClickable, IModelWithImages
{
    override fun getImageList() = images
    override fun getViewType() = R.id.viewType_Artist

    //todo some artists dont have ids!!! e.g. "Alice Cooper feat. Roger Glover"
    // consider this when calculating DiffUtil!!!
    // handle situation where the id is empty and also completely doesnt exist!

    //todo do i even need an id if its unreliable????
    // can i use something else as a database id???
    //      maybe use an auto generated id, but actually treat the name as an id?

    //todo assume all variables can be null!

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