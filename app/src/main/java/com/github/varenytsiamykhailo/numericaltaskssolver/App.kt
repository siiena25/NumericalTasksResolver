package com.github.varenytsiamykhailo.numericaltaskssolver

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import com.github.varenytsiamykhailo.numericaltaskssolver.di.appModule

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            modules(appModule)
            androidContext(this@App)
        }
    }
}
