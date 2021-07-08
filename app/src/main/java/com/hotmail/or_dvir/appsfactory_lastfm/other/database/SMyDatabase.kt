package com.hotmail.or_dvir.appsfactory_lastfm.other.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album
import com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos.IDaoAlbums

@Database(entities = [Album::class], version = 1)
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
                    )
                        //todo remove this in production!!!
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}