package com.stockbit.remote

import com.stockbit.model.WatchlistModel

/**
 * Implementation of [ExampleService] interface
 */
class WatchlistDatasource(private val watchlistService: WatchlistService) {

    suspend fun fetchWatclistAsync(page : Int) : List<WatchlistModel> {
        val response = watchlistService.fetchWatchlistAsync(page)
        if (response.message?.uppercase().equals("success".uppercase())){
            val watchlist: MutableList<WatchlistModel> = mutableListOf()
            response.data?.forEach {
                watchlist.add(
                    WatchlistModel(
                        it.coinInfo.id,
                        it.coinInfo.name,
                        it.coinInfo.fullname,
                        it.display?.usd?.price,
                        it.display?.usd?.changeHour,
                        it.display?.usd?.changePtcHour,
                    )
                )
            }
            return watchlist
        }else {
            return emptyList()
        }
    }

}