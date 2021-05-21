package com.aliensquad.trafficlightmonitor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.data.repository.TrafficLightRepository
import com.aliensquad.trafficlightmonitor.utils.RadiusConfiguration
import com.aliensquad.trafficlightmonitor.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DashboardViewModel(private val trafficLightRepository: TrafficLightRepository) : ViewModel() {

    private val _trafficLightsState = MutableLiveData<Resource<List<TrafficLight>>>().apply {
        value = Resource.loading(null)
    }

    val trafficLightsState: LiveData<Resource<List<TrafficLight>>> get() = _trafficLightsState

    private fun getTrafficLights(radius: RadiusConfiguration.Radius): Job {
        return viewModelScope.launch {
            _trafficLightsState.value = Resource.loading(null)
            _trafficLightsState.value = trafficLightRepository.getTrafficLights(radius)
        }
    }
}