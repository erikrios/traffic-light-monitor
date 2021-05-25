package com.aliensquad.trafficlightmonitor.core.di

import com.aliensquad.trafficlightmonitor.core.BuildConfig
import com.aliensquad.trafficlightmonitor.core.data.repository.TrafficLightRepository
import com.aliensquad.trafficlightmonitor.core.data.repository.TrafficLightRepositoryImpl
import com.aliensquad.trafficlightmonitor.core.data.source.local.LocalDataSource
import com.aliensquad.trafficlightmonitor.core.data.source.remote.RemoteDataSource
import com.aliensquad.trafficlightmonitor.core.data.source.remote.network.TrafficLightMonitorApiHelper
import com.aliensquad.trafficlightmonitor.core.data.source.remote.network.TrafficLightMonitorApiHelperImpl
import com.aliensquad.trafficlightmonitor.core.data.source.remote.network.TrafficLightMovitorApiService
import com.aliensquad.trafficlightmonitor.core.utils.NetworkHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .addHeader("API-Key", BuildConfig.API_KEY)
                .build()
            return@Interceptor chain.proceed(request)
        }
    }
    single<Gson> {
        GsonBuilder()
            .setLenient()
            .create()
    }
    single {
        if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .callTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(get<Interceptor>())
                .build()
        } else {
            OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(get<Interceptor>())
                .build()
        }
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .client(get())
            .build()
    }
    single {
        get<Retrofit>().create(TrafficLightMovitorApiService::class.java)
    }
    single<TrafficLightMonitorApiHelper> {
        TrafficLightMonitorApiHelperImpl(get())
    }
}

val repositoryModule = module {
    single { LocalDataSource() }
    single { RemoteDataSource(get()) }
    factory { NetworkHelper(androidContext()) }
    single<TrafficLightRepository> { TrafficLightRepositoryImpl(get(), get(), get()) }
}