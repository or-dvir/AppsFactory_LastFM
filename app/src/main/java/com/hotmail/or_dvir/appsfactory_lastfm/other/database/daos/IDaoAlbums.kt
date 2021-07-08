package com.hotmail.or_dvir.appsfactory_lastfm.other.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface IDaoAlbums
{

    //todo keep for reference
//    //returns the Ids of the rows (not the id of the objects, but of the rows).
//    //NOTE:
//    //if you need to know how many rows were ACTUALLY added
//    //you need to count how many items in this array are NOT "-1"
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertSync(seasonToAdd: List<SeasonEntity>): List<Long>
//
//    @Delete
//    fun removeSync(seasonsToRemove: List<SeasonEntity>): Int
//
//    @Query("delete from table_seasons where showIdDb in (:showIds)")
//    fun removeSeasonsForShowsSync(showIds: List<Long>)
//
//    @Update
//    fun updateSync(seasons: List<SeasonEntity>)
//
//    @Query("select * from table_seasons")
//    fun getAll(): LiveData<List<SeasonEntity>>
//    @Query("select * from table_seasons")
//    fun getAllSync(): List<SeasonEntity>
//
//    @Query("select * from table_seasons where showIdDb = :showId order by seasonNumDb")
//    fun getAllForShowByNumberSync(showId: Long): List<SeasonEntity>
//
//    @Query("delete from table_seasons")
//    fun deleteEntireTable()
}