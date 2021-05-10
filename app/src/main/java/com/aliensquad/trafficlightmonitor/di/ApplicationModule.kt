package com.aliensquad.trafficlightmonitor.di

import com.aliensquad.trafficlightmonitor.data.source.DataSource
import com.aliensquad.trafficlightmonitor.data.source.local.LocalDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    @Named("localDataSource")
    fun provideLocalDataSource(localDataSource: LocalDataSource): DataSource =
        localDataSource
}