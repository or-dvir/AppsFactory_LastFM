package com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album

@Dao
interface IDaoAlbums
{
    /**
     * note that this function would use the primary key of Album
     * which is NOT a reliable id (see Album.dbUUID).
     * for simplicity, we use this default method for insertion,
     * but further checks need to be made before calling it or we may end up with
     * bad data.
     *
     * @return Long the id of the inserted row (NOT the object!), or -1 if failed
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(album: Album): Long

    /**
     * @return Int number of rows deleted
     */
    @Query("delete from table_favoriteAlbums where dbUUID=:dbUUID")
    suspend fun delete(dbUUID: String): Int

    @Query("select * from table_favoriteAlbums")
    fun getFavoriteAlbums(): LiveData<List<Album>>

    @Query("select (count(*) > 0) from table_favoriteAlbums where dbUUID=:dbUUID limit 1")
    fun isInFavorites(dbUUID: String): Boolean
}