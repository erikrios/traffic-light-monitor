package com.aliensquad.trafficlightmonitor.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.data.repository.TrafficLightRepository
import com.aliensquad.trafficlightmonitor.utils.Resource
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailsViewModel(private val trafficLightRepository: TrafficLightRepository) : ViewModel() {

    private val _trafficLightState = MutableLiveData<Resource<TrafficLight>>().apply {
        value = Resource.loading(null)
    }

    val trafficLightState: LiveData<Resource<TrafficLight>> get() = _trafficLightState

    fun getTrafficLight(id: Long): Job {
        return viewModelScope.launch {
            _trafficLightState.value = Resource.loading(null)
            _trafficLightState.value = trafficLightRepository.getTrafficLightDetails(id)
        }
    }
}