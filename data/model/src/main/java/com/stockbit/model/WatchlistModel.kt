package com.stockbit.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WatchlistModel(
    @PrimaryKey val id: String,
    val name: String?,
    val fullname: String?,
    val price: String?,
    val changeHour: String?,
    val changePtcHour: String?,
)


