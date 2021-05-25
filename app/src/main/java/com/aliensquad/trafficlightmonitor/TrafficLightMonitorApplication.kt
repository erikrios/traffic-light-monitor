package com.aliensquad.trafficlightmonitor

import android.app.Application
import com.aliensquad.trafficlightmonitor.core.di.networkModule
import com.aliensquad.trafficlightmonitor.core.di.repositoryModule
import com.aliensquad.trafficlightmonitor.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class TrafficLightMonitorApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@TrafficLightMonitorApplication)
            modules(
                listOf(
                    networkModule,
                    repositoryModule,
                    viewModelModule
                )
            )
        }
    }
}