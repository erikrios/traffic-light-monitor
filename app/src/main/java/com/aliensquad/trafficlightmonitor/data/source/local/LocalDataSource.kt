package com.aliensquad.trafficlightmonitor.data.source.local

import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.data.source.DataSource
import com.aliensquad.trafficlightmonitor.utils.Resource

class LocalDataSource : DataSource {

    private val trafficLightCaches = mutableSetOf<TrafficLight>()

    override suspend fun getTrafficLights(): Resource<List<TrafficLight>> =
        Resource.success(trafficLightCaches.toList())

    override suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight> {
        val trafficLight = trafficLightCaches.find { id == it.id }
        return Resource.success(trafficLight)
    }

    fun addCaches(trafficLights: List<TrafficLight>) = trafficLightCaches.addAll(trafficLights)
}