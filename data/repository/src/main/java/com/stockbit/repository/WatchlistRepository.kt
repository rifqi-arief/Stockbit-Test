package com.stockbit.repository

import com.stockbit.local.dao.ExampleDao
import com.stockbit.local.dao.WatchlistDao
import com.stockbit.model.BaseResponse
import com.stockbit.model.ExampleModel
import com.stockbit.model.WatchlistModel
import com.stockbit.remote.ExampleDatasource
import com.stockbit.remote.WatchlistDatasource
import com.stockbit.remote.WatchlistService
import com.stockbit.repository.utils.Resource
import com.stockbit.repository.utils.networkBoundResource
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun getWatchlist(): Flow<Resource<List<WatchlistModel>>>
}

class WatchlistRepositoryImpl(private val remote: WatchlistDatasource, private val db: WatchlistDao): WatchlistRepository {
    override suspend fun getWatchlist(): Flow<Resource<List<WatchlistModel>>> =
        networkBoundResource(
            query = {
                db.getAll()
            },
            fetch = {

                remote.fetchWatclistAsync()
            },
            saveFetchResult = { response ->
                db.deleteAll()
                response?.let { db.insert(it) }
            }
        ) as Flow<Resource<List<WatchlistModel>>>
}