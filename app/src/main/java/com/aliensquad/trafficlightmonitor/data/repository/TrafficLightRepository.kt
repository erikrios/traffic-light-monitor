package com.aliensquad.trafficlightmonitor.data.repository

import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.utils.Resource

interface TrafficLightRepository {
    suspend fun getTrafficLights(radius: RadiusConfiguration.Radius): Resource<List<TrafficLight>>
    suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight>
}