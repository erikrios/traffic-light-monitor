package com.aliensquad.trafficlightmonitor.core.data.source.remote

import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.data.source.DataSource
import com.aliensquad.trafficlightmonitor.core.data.source.remote.network.TrafficLightMonitorApiHelper
import com.aliensquad.trafficlightmonitor.core.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.core.utils.Resource

class RemoteDataSource(private val apiHelper: TrafficLightMonitorApiHelper) : DataSource {

    override suspend fun getTrafficLights(
        radius: RadiusConfiguration.Radius,
        latitude: Double,
        longitude: Double
    ): Resource<List<TrafficLight>> {
        return try {
            val response = apiHelper.getTrafficLights(radius.distance, latitude, longitude)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(
                    "Error to get data with response code ${response.code()}",
                    null
                )
            }
        } catch (e: Exception) {
            Resource.error("Something went wrong.", null)
        }
    }

    override suspend fun getTrafficLightDetails(id: Long): Resource<TrafficLight> {
        return try {
            val response = apiHelper.getTrafficLightDetails(id)
            if (response.isSuccessful) {
                Resource.success(response.body()?.data)
            } else {
                Resource.error(
                    "Error to get data with response code ${response.code()}",
                    null
                )
            }
        } catch (e: Exception) {
            Resource.error("Something went wrong.", null)
        }
    }
}