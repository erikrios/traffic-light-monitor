package com.aliensquad.trafficlightmonitor.core.data.source.remote.network

import com.aliensquad.trafficlightmonitor.core.data.model.Result
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TrafficLightApiService {

    @GET("traffics")
    suspend fun getTrafficLights(
        @Query("radius")
        radius: Int,
        @Query("latitude")
        latitude: Double,
        @Query("longitude")
        longitude: Double
    ): Response<Result<List<TrafficLight>>>


    @GET("traffics/{id}")
    suspend fun getTrafficLightDetails(@Path("id") id: Long): Response<Result<TrafficLight>>
}