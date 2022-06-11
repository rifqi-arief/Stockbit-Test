package com.stockbit.hiring

import android.app.Application
import com.stockbit.hiring.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

open class App: Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi()
    }

    // CONFIGURATION ---
    open fun configureDi() =
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(
                provideComponent()
            )
        }

    // PUBLIC API ---
    open fun provideComponent() = appComponent
}