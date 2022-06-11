package com.watchlist.watchlist.di

import com.watchlist.watchlist.viewmodel.WatchlistViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val watchlistModule = module {
    viewModel { WatchlistViewModel(get())}
}