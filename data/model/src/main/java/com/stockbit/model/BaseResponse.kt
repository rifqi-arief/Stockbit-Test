package com.stockbit.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class BaseResponse<T>(
    @SerializedName("Message") val message: String?,
    @SerializedName("Data") val data: @RawValue T?
) : Parcelable