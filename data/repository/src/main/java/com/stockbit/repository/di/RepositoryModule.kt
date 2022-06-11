package com.stockbit.repository.di

import com.stockbit.repository.*
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val repositoryModule = module {
    factory { AppDispatchers(Dispatchers.Main, Dispatchers.IO) }
    factory { ExampleRepositoryImpl(get(), get()) as ExampleRepository }
    factory { WatchlistRepositoryImpl(get(), get()) as WatchlistRepository}
}