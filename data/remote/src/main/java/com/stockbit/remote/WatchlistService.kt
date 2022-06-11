package com.stockbit.remote

import com.stockbit.model.BaseResponse
import com.stockbit.model.WatchlistModel
import com.stockbit.model.WatchlistResponse
import retrofit2.http.GET


interface WatchlistService {

    @GET("data/top/totaltoptiervolfull?limit=50&tsym=USD")
    suspend fun fetchWatchlistAsync(): BaseResponse<List<WatchlistResponse>>

}