package com.aliensquad.trafficlightmonitor.di

import com.aliensquad.trafficlightmonitor.dashboard.DashboardViewModel
import com.aliensquad.trafficlightmonitor.details.DetailsViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { DashboardViewModel(get()) }
    viewModel { DetailsViewModel(get()) }
}