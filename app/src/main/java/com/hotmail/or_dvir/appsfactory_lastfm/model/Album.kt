package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.appsfactory_lastfm.other.isBlankOrEquals
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
    @ColumnInfo(name = "dbPrimaryKey")
    @Json(name = "dbPrimaryKey")
    @PrimaryKey(autoGenerate = true)
    var dbPrimaryKey: Long = 0L
) : IDxItemClickable, IModelWithImages
{
    //todo index the field names used to delete in the database
    //todo some album names are "(null)"
    // handle this in the recycler view (search for albums by "cher")

    private companion object
    {
        private const val LASTFM_NULL = "(null)"
    }

    /**
     * unfortunately there doesn't seem to be a reliable field to use as id from the LastFm API.
     * even "mbid" may bu empty, null, or completely missing!
     *
     * note that we CANNOT use [dbPrimaryKey] because that is not a value retrieved
     * from the server so we cannot use it to identify existing rows.
     *
     * so instead we use a combination of the artist name and the album name.
     * note that if one of these values is null or empty, the object does not have a unique id
     * and therefore it cannot be saved in the database.
     */
    @ColumnInfo(name = "dbUUID")
    @Json(name = "dbUUID")
    //made var and not val for convenience (otherwise Room complains).
    //in real app, a better solution would be used
    var dbUUID = name?.let { albumName ->
        artist?.name?.let { artistName ->
            if (artistName.isBlankOrEquals(LASTFM_NULL) || albumName.isBlankOrEquals(LASTFM_NULL))
            {
                null
            } else
            {
                "$artistName$albumName"
            }
        }
    }

    fun canBeStoredInDb() = dbUUID != null

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

    class DiffCallback(private val oldList: List<Album>, private val newList: List<Album>) :
        DiffUtil.Callback()
    {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].dbUUID == newList[newItemPosition].dbUUID

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