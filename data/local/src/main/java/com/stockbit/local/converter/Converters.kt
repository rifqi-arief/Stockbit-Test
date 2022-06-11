package com.stockbit.local.converter

import androidx.room.TypeConverter
import com.stockbit.model.WatchlistModel
import com.stockbit.model.WatchlistResponse
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

}