package com.stockbit.model

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class WatchlistResponse(
    @SerializedName("CoinInfo") val coinInfo: CoinInfoResponse,
    @SerializedName("DISPLAY") val display: DisplayResponse?
) : Parcelable

@Parcelize
data class CoinInfoResponse(
    @SerializedName("Id") val id: String,
    @SerializedName("Name") val name: String?,
    @SerializedName("FullName") val fullname: String?
) : Parcelable

@Parcelize
data class DisplayResponse(
    @SerializedName("USD") val usd: UsdResponse?,
) : Parcelable

@Parcelize
data class UsdResponse(
    @SerializedName("PRICE") val price: String,
    @SerializedName("CHANGEHOUR") val changeHour: String,
    @SerializedName("CHANGEPCTHOUR") val changePtcHour: String,
) : Parcelable

