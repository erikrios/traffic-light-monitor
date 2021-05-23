package com.aliensquad.trafficlightmonitor.data.source.local

import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.data.source.DataSource
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.utils.Resource

class LocalDataSource : DataSource {

    private val trafficLightCaches = mutableMapOf<RadiusConfiguration.Radius, List<TrafficLight>>()

    override suspend fun getTrafficLights(
        radius: RadiusConfiguration.Radius,
        latitude: Double,
        longitude: Double
    ): Resource<List<TrafficLight>> {
        val trafficLights = trafficLightCaches[radius]
        if (trafficLights.isNullOrEmpty()) {
            return Resource.error("The data is null or empty.", null)
        }
        return Resource.success(trafficLights)
    }

    override suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight> {
        val trafficLights = mutableListOf<TrafficLight>()
        val listOfTrafficLights = trafficLightCaches.values.toList()
        listOfTrafficLights.forEach {
            it.forEach { trafficLight ->
                trafficLights.add(trafficLight)
            }
        }

        val trafficLight = trafficLights.find { it.id == id }
        return trafficLight?.let { Resource.success(trafficLight) }
            ?: Resource.error("The data not found", null)
    }

    fun addCaches(radius: RadiusConfiguration.Radius, trafficLights: List<TrafficLight>) =
        trafficLightCaches.put(radius, trafficLights)
}