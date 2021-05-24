package com.aliensquad.trafficlightmonitor.core.data.source.remote.network

class TrafficLightApiHelperImpl(private val apiService: TrafficLightApiService) :
    TrafficLightMonitorApiHelper {

    override suspend fun getTrafficLights(
        radius: Int,
        latitude: Double,
        longitude: Double
    ) = apiService.getTrafficLights(radius, latitude, longitude)

    override suspend fun getTrafficLightDetails(id: Long) = apiService.getTrafficLightDetails(id)
}