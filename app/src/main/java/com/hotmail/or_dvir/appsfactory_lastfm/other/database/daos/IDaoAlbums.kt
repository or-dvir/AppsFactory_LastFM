package com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hotmail.or_dvir.appsfactory_lastfm.model.Album

/**
 * a DAO
 */
@Dao
interface IDaoAlbums
{
    /**
     * inserts the given [Album] into the database.
     *
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
     * deletes the the [Album] with the given [dbUUID] from the database
     * @return Int number of rows deleted
     */
    @Query("delete from table_favoriteAlbums where dbUUID=:dbUUID")
    suspend fun delete(dbUUID: String): Int

    /**
     * returns a [LiveData] object for all [Album]s the user has marked as favorites
     */
    @Query("select * from table_favoriteAlbums")
    fun getFavoriteAlbums(): LiveData<List<Album>>

    /**
     * returns the [Album] with the given [dbUUID] from the database
     */
    @Query("select * from table_favoriteAlbums where dbUUID=:dbUUID limit 1")
    fun getAlbum(dbUUID: String): Album?

    /**
     * returns whether or not an album with the given [dbUUID] is marked as favorite
     */
    @Query("select (count(*) > 0) from table_favoriteAlbums where dbUUID=:dbUUID limit 1")
    fun isInFavorites(dbUUID: String): Boolean
}