package com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album

@Dao
interface IDaoAlbums
{
    /**
     * @return Long the id of the inserted row (NOT the object!), or -1 if failed
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: Album): Long

    /**
     * @return Int number of rows deleted
     */
    @Delete
    suspend fun delete(album: Album): Int
}