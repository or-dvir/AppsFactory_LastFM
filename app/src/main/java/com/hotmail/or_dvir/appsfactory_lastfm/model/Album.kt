package com.hotmail.or_dvir.appsfactory_lastfm.model

import androidx.recyclerview.widget.DiffUtil
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hotmail.or_dvir.appsfactory_lastfm.R
import com.hotmail.or_dvir.dxclick.IDxItemClickable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * a class representing an [Album]
 */
@Entity(
    //index the column used to identify albums so our queries will be more efficient
    indices = [Index(value = [Album.COLUMN_DB_UUID])],
    tableName = "table_favoriteAlbums"
)
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
    var tracks: Tracks?,
    @ColumnInfo(name = "image")
    @Json(name = "image")
    val images: List<Image> = listOf(),
    @ColumnInfo(name = "dbPrimaryKey")
    @Json(name = "dbPrimaryKey")
    @PrimaryKey(autoGenerate = true)
    var dbPrimaryKey: Long = 0L
) : IDxItemClickable, IModelWithImages
{
    companion object
    {
        private const val LASTFM_NULL = "(null)"
        internal const val COLUMN_DB_UUID = "dbUUID"

        /**
         * returns the unique database ID for an [Album] with the given [artistName] and [albumName]
         * @see dbUUID
         */
        fun toDbUuid(artistName: String, albumName: String) = "${artistName}$albumName"
    }

    //IMPORTANT NOTE:
    //unfortunately there doesn't seem to be a reliable field to use as id from the LastFm API.
    //even "mbid" may be empty, null, or completely missing!
    //note that we CANNOT use dbPrimaryKey because that is not a value retrieved
    //from the server so we cannot use it to identify already existing rows.
    //so instead we use a combination of the artist name and the album name
    //(which are required for other API requests anyway).
    //this means that if one of these values is null or empty, the object does not have a unique id
    //and therefore it cannot be saved in the database.
    /**
     * the unique database ID for this [Album] comprised of the album name and artist name,
     * or `null` if this [Album] does not contain valid values to be uniquely identified
     * in the database
     * @see canBeStoredInDb
     * @see toDbUuid
     * @see canBeStoredInDb
     * @see isNameValid
     * @see isArtistNameValid
     */
    @ColumnInfo(name = COLUMN_DB_UUID)
    @Json(name = "dbUUID")
    //for simplicity, made var and not val (otherwise Room complains).
    var dbUUID =
        if (isNameValid() && isArtistNameValid())
        {
            //non of these should be null because of the "if" above
            toDbUuid(artist!!.name!!, name!!)
        } else
        {
            null
        }

    /**
     * returns whether or not this [Album] holds valid values for creating the [dbUUID].
     * if the returned value is `false`, this [Album] cannot be uniquely identified and therefore
     * cannot be stored in the local database
     * @see dbUUID
     */
    fun canBeStoredInDb() = dbUUID != null

    /**
     * a helper function to determine whether or not the name of this album is valid.
     * this is required for creating a valid [dbUUID]
     */
    fun isNameValid() = when
    {
        name.isNullOrBlank() -> false
        name == LASTFM_NULL -> false
        else -> true
    }

    /**
     * a helper function to determine whether or not the name of the artist of this album
     * is valid.
     * this is required for creating a valid [dbUUID]
     */
    fun isArtistNameValid() = when
    {
        artist?.name.isNullOrBlank() -> false
        artist?.name == LASTFM_NULL -> false
        else -> true
    }

    /**
     * returns a list of [Image]s for this [Album]
     */
    override fun getImageList() = images

    /**
     * a unique id representing this object (used only internally)
     */
    override fun getViewType() = R.id.viewType_Album

    /**
     * returns this [Album]s' list of [Tracks.Track]s.
     * @param sorted Boolean whether or not the returned list should be sorted by the order
     * the [Tracks.Track] appear on the album
     */
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

    /**
     * used for [DiffUtil] calculations.
     */
    class DiffCallback(private val oldList: List<Album>, private val newList: List<Album>) :
        DiffUtil.Callback()
    {
        /**
         * 2 [Album]s are considered the same, if the [Album.equals] method return `true`
         */
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            oldList[oldItemPosition].dbUUID == newList[newItemPosition].dbUUID

        /**
         * the contents of 2 [Album]s is considered the same, if they have the same name,
         * and same artist name
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]

            return old.name == new.name &&
                    old.artist?.name == new.artist?.name
        }

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
    }
}