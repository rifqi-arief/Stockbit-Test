package com.stockbit.remote

import com.stockbit.model.BaseResponse
import com.stockbit.model.WatchlistModel
import com.stockbit.model.WatchlistResponse
import retrofit2.http.GET
import retrofit2.http.Query


interface WatchlistService {

    @GET("data/top/totaltoptiervolfull?limit=10&tsym=USD")
    suspend fun fetchWatchlistAsync(
        @Query("page") page: Int
    ): BaseResponse<List<WatchlistResponse>>

}