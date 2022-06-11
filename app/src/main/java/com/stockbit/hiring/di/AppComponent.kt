package com.stockbit.hiring.di

import com.stockbit.local.di.localModule
import com.stockbit.remote.di.createRemoteModule
import com.stockbit.repository.di.repositoryModule
import com.watchlist.watchlist.di.watchlistModule

val appComponent= listOf(
    createRemoteModule("https://min-api.cryptocompare.com/"),
    repositoryModule,
    localModule,
    watchlistModule
)