package com.cmunaro.madrugada

import android.app.Application
import com.cmunaro.madrugada.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@Suppress("unused")
class MadrugadaApplication : Application(){
    override fun onCreate() {
        super.onCreate()
        setupKoin()
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MadrugadaApplication)
            modules(viewModelModule)
        }
    }
}