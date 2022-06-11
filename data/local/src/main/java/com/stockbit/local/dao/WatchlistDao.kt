package com.stockbit.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.stockbit.model.WatchlistModel
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface WatchlistDao {

    @Query("SELECT * FROM WatchlistModel")
    fun getAll() : Flow<List<WatchlistModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(data: List<WatchlistModel>)

    @Query("DELETE FROM WatchlistModel")
    fun deleteAll()

}

