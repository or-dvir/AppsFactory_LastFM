package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "mbid")
    val id: String?,
    @Json(name = "name")
    val name: String,
    @Json(name = "artist")
    val artist: Artist,
    @Json(name = "image")
    val images: List<Image> = listOf()
) : IDxItemClickable, IModelWithImages
{
    //todo some albums dont have ids!!!
    // consider this when calculating DiffUtil!!!
    // handle situation where the id is empty and also completely doesnt exist!

    //todo assume all variables can be null!

    override fun getImageList() = images
    override fun getViewType() = R.id.viewType_Album

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    //todo copied from Artist class. can i make a generic one that only compares id?
    // the contents of the data (with the same id) does not change...
    class DiffCallback(private val oldList: List<Album>, private val newList: List<Album>) :
        DiffUtil.Callback()
    {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].id == newList[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            //todo should i add images here too?
            // what about artist?
            return old.name == new.name
        }

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    }
}