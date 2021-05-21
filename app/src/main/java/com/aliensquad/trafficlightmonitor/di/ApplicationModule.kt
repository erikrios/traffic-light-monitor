package com.aliensquad.trafficlightmonitor.di

import com.aliensquad.trafficlightmonitor.data.repository.TrafficLightRepository
import com.aliensquad.trafficlightmonitor.data.repository.TrafficLightRepositoryImpl
import com.aliensquad.trafficlightmonitor.data.source.local.LocalDataSource
import com.aliensquad.trafficlightmonitor.data.source.remote.RemoteDataSource
import com.aliensquad.trafficlightmonitor.ui.viewmodel.DashboardViewModel
import com.aliensquad.trafficlightmonitor.ui.viewmodel.DetailsViewModel
import com.aliensquad.trafficlightmonitor.utils.NetworkHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val repositoryModule = module {
    single { LocalDataSource() }
    single { RemoteDataSource() }
    factory { NetworkHelper(androidContext()) }
    single<TrafficLightRepository> { TrafficLightRepositoryImpl(get(), get(), get()) }
}

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}