package com.aliensquad.trafficlightmonitor.data.source.remote

import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.data.source.DataSource
import com.aliensquad.trafficlightmonitor.utils.DummyData.generateTrafficLights
import com.aliensquad.trafficlightmonitor.utils.DummyData.getTrafficLight
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.utils.Resource

class RemoteDataSource : DataSource {

    override suspend fun getTrafficLights(radius: RadiusConfiguration.Radius): Resource<List<TrafficLight>> {
        val trafficLights = generateTrafficLights().shuffled()
        return Resource.success(trafficLights)
    }

    override suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight> {
        val trafficLight = getTrafficLight(id)
        return Resource.success(trafficLight)
    }
}