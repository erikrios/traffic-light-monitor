package com.aliensquad.trafficlightmonitor.core.data.repository

import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.core.utils.Resource

interface TrafficLightRepository {
    suspend fun getTrafficLights(
        radius: RadiusConfiguration.Radius,
        latitude: Double,
        longitude: Double
    ): Resource<List<TrafficLight>>

    suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight>
}