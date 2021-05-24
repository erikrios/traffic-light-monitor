package com.aliensquad.di

import com.aliensquad.trafficlightmonitor.core.data.repository.TrafficLightRepository
import com.aliensquad.trafficlightmonitor.core.data.repository.TrafficLightRepositoryImpl
import com.aliensquad.trafficlightmonitor.core.data.source.local.LocalDataSource
import com.aliensquad.trafficlightmonitor.core.data.source.remote.RemoteDataSource
import com.aliensquad.utils.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {
    single { LocalDataSource() }
    single { RemoteDataSource() }
    factory { NetworkHelper(androidContext()) }
    single<TrafficLightRepository> { TrafficLightRepositoryImpl(get(), get(), get()) }
}