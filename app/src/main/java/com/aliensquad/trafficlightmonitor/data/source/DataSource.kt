package com.aliensquad.trafficlightmonitor.data.source

import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.utils.Resource

interface DataSource {
    suspend fun getTrafficLights(): Resource<List<TrafficLight>>
    suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight>
}