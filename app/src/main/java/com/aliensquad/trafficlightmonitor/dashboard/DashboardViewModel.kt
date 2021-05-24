package com.aliensquad.trafficlightmonitor.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.data.repository.TrafficLightRepository
import com.aliensquad.trafficlightmonitor.core.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.core.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel(private val trafficLightRepository: TrafficLightRepository) : ViewModel() {

    private val _trafficLightsState = MutableLiveData<Resource<List<TrafficLight>>>().apply {
        value = Resource.loading(null)
    }

    val trafficLightsState: LiveData<Resource<List<TrafficLight>>> get() = _trafficLightsState

    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()
    val latitude: LiveData<Double> get() = _latitude
    val longitude: LiveData<Double> get() = _longitude

    fun getTrafficLights(
        radius: RadiusConfiguration.Radius,
        latitude: Double,
        longitude: Double
    ): Job {
        return viewModelScope.launch {
            _trafficLightsState.value = Resource.loading(null)
            _trafficLightsState.value =
                trafficLightRepository.getTrafficLights(radius, latitude, longitude)
        }
    }

    fun setLatitudeAndLongitude(latitude: Double, longitude: Double) {
        _latitude.value = latitude
        _longitude.value = longitude
    }
}