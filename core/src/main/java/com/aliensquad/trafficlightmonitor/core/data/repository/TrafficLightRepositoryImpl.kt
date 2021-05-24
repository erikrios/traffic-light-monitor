package com.aliensquad.trafficlightmonitor.core.data.repository

import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.data.source.local.LocalDataSource
import com.aliensquad.trafficlightmonitor.core.data.source.remote.RemoteDataSource
import com.aliensquad.trafficlightmonitor.core.utils.NetworkHelper
import com.aliensquad.trafficlightmonitor.core.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.core.utils.Resource
import com.aliensquad.trafficlightmonitor.core.utils.Status

class TrafficLightRepositoryImpl(
    private val networkHelper: NetworkHelper,
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : TrafficLightRepository {

    override suspend fun getTrafficLights(
        radius: RadiusConfiguration.Radius,
        latitude: Double,
        longitude: Double
    ): Resource<List<TrafficLight>> {
        if (networkHelper.isNetworkConnected()) {
            val trafficLightsResource =
                remoteDataSource.getTrafficLights(radius, latitude, longitude)
            if (trafficLightsResource.status == Status.SUCCESS) localDataSource.addCaches(
                radius,
                trafficLightsResource.data as List<TrafficLight>
            )
            return trafficLightsResource
        } else {
            val trafficLightResource = localDataSource.getTrafficLights(radius, latitude, longitude)
            return when (trafficLightResource.status) {
                Status.SUCCESS -> trafficLightResource
                else -> Resource.error(
                    "Couldn't reach the server. Check your internet connection",
                    null
                )
            }
        }
    }

    override suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight> {
        return if (networkHelper.isNetworkConnected()) {
            remoteDataSource.getTrafficLightDetails(id)
        } else {
            val trafficLightResource = localDataSource.getTrafficLightDetails(id)
            when (trafficLightResource.status) {
                Status.SUCCESS -> trafficLightResource
                else -> Resource.error(
                    "Couldn't reach the server. Check your internet connection",
                    null
                )
            }
        }
    }
}