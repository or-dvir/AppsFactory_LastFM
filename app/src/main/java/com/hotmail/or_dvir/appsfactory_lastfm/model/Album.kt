package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Entity(tableName = "table_favoriteAlbums")
@JsonClass(generateAdapter = true)
data class Album(
    //note: the names of the columns and json just HAPPEN to be the same.
    //they should NOT be extracted to a constant for flexibility, so that
    //they may change independently of each other
    @ColumnInfo(name = "name")
    @Json(name = "name")
    val name: String?,
    @ColumnInfo(name = "artist")
    @Json(name = "artist")
    val artist: Artist?,
    @ColumnInfo(name = "tracks")
    @Json(name = "tracks")
    val tracks: Tracks?,
    @ColumnInfo(name = "image")
    @Json(name = "image")
    val images: List<Image> = listOf(),
    @ColumnInfo(name = "dbId")
    @Json(name = "dbId")
    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0L
) : IDxItemClickable, IModelWithImages
{

    //todo how can i save Tracks and list of Image to DB?!

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