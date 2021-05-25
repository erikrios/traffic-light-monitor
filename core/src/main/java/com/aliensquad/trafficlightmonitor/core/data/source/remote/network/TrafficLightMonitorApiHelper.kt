package com.aliensquad.trafficlightmonitor.core.data.source.remote.network

import com.aliensquad.trafficlightmonitor.core.data.model.Result
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import retrofit2.Response

interface TrafficLightMonitorApiHelper {
    suspend fun getTrafficLights(
        radius: Int,
        latitude: Double,
        longitude: Double
    ): Response<Result<List<TrafficLight>>>

    suspend fun getTrafficLightDetails(id: Long): Response<Result<TrafficLight>>
}