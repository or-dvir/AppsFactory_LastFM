package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Album(
    @Json(name = "name")
    val name: String?,
    @Json(name = "artist")
    val artist: Artist?,
    @Json(name = "image")
    val images: List<Image> = listOf(),
    @Json(name = "tracks")
    val tracks: Tracks?
) : IDxItemClickable, IModelWithImages
{
    override fun getImageList() = images
    override fun getViewType() = R.id.viewType_Album

    fun getTracks(sorted: Boolean = true) =
        tracks?.trackList?.apply {
            if (sorted)
            {
                sortedWith(compareBy(nullsLast()) { it.attributes?.rank })
            }
        }

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
            oldList[oldItemPosition] == newList[newItemPosition]

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