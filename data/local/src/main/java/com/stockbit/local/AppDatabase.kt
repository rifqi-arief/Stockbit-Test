package com.stockbit.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.stockbit.local.converter.Converters
import com.stockbit.local.dao.ExampleDao
import com.stockbit.local.dao.WatchlistDao
import com.stockbit.model.ExampleModel
import com.stockbit.model.WatchlistModel

@Database(entities = [ExampleModel::class, WatchlistModel::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    // DAO
    abstract fun exampleDao(): ExampleDao
    abstract fun watchlistDao(): WatchlistDao
//    abstract fun watchlistDao(): WatchlistDao

    companion object {

        fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "App.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build()
    }
}