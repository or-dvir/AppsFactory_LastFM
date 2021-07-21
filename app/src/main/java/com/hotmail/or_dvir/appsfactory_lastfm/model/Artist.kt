package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * a class representing an [Artist]
 */
@JsonClass(generateAdapter = true)
data class Artist(
    @Json(name = "name")
    val name: String?,
    @Json(name = "image")
    val images: List<Image> = listOf()
) : IDxItemClickable, IModelWithImages
{
    /**
     * returns a list of [Image]s for this [Artist]
     */
    override fun getImageList() = images

    /**
     * a unique id representing this object (used only internally)
     */
    override fun getViewType() = R.id.viewType_Artist

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    /**
     * used for [DiffUtil] calculations.
     */
    class DiffCallback(private val oldList: List<Artist>, private val newList: List<Artist>) :
        DiffUtil.Callback()
    {
        /**
         * 2 [Artist]s are considered the same, if the [Artist.equals] method return `true`
         */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition] == newList[newItemPosition]

        /**
         * the contents of 2 [Artist]s is considered the same, if they have the same name
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            return old.name == new.name
        }

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    }
}