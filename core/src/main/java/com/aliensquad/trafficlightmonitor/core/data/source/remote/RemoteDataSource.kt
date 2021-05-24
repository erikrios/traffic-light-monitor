package com.aliensquad.trafficlightmonitor.core.data.source.remote

import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.data.source.DataSource
import com.aliensquad.utils.DummyData.generateTrafficLights
import com.aliensquad.utils.DummyData.getTrafficLight
import com.aliensquad.utils.RadiusConfiguration
import com.aliensquad.utils.Resource

class RemoteDataSource : DataSource {

    override suspend fun getTrafficLights(
        radius: RadiusConfiguration.Radius,
        latitude: Double,
        longitude: Double
    ): Resource<List<TrafficLight>> {
        val trafficLights = generateTrafficLights().shuffled()
        return Resource.success(trafficLights)
    }

    override suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight> {
        val trafficLight = getTrafficLight(id)
        return Resource.success(trafficLight)
    }
}