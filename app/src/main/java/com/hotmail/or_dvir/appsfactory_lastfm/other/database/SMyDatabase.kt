package com.hotmail.or_dvir.appsfactory_lastfm.other.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.model.Artist
import com.hotmail.or_dvir.appsfactory_lastfm.model.Image
import com.hotmail.or_dvir.appsfactory_lastfm.model.Tracks
import com.hotmail.or_dvir.appsfactory_lastfm.other.SMyMoshi
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.SMyDatabase.RoomConverters
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Types

@Database(entities = [Album::class], version = 2)
@TypeConverters(RoomConverters::class)
abstract class SMyDatabase : RoomDatabase()
{
    abstract fun daoAlbums(): IDaoAlbums

    companion object
    {
        @Volatile
        private var INSTANCE: SMyDatabase? = null

        fun getInstance(context: Context): SMyDatabase
        {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null)
                {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SMyDatabase::class.java,
                        "LastFM_database"
                    ).build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }

    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////
    ////////////////////////////////////////////////////

    @Suppress("unused")
    class RoomConverters
    {
        private val adapterArtist: JsonAdapter<Artist>
        private val adapterTracks: JsonAdapter<Tracks>
        private val adapterImageList: JsonAdapter<List<Image>>

        init
        {
            SMyMoshi.instance.apply {
                adapterArtist = adapter(Artist::class.java)
                adapterTracks = adapter(Tracks::class.java)

                val typeImageList =
                    Types.newParameterizedType(MutableList::class.java, Image::class.java)
                adapterImageList = adapter(typeImageList)
            }
        }

        //region artist
        @TypeConverter
        fun toArtist(json: String) = adapterArtist.fromJson(json)

        @TypeConverter
        fun fromArtist(artist: Artist?): String = adapterArtist.toJson(artist)
        //endregion

        //region tracks
        @TypeConverter
        fun toTracks(json: String) = adapterTracks.fromJson(json)

        @TypeConverter
        fun fromTracks(tracks: Tracks?): String = adapterTracks.toJson(tracks)
        //endregion

        //region image list
        @TypeConverter
        fun toImageList(json: String) = adapterImageList.fromJson(json)

        @TypeConverter
        fun fromImageList(images: List<Image>?): String = adapterImageList.toJson(images)
        //endregion
    }
}