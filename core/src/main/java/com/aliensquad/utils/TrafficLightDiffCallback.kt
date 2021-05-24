package com.aliensquad.utils

import androidx.recyclerview.widget.DiffUtil
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight

class TrafficLightDiffCallback(
    private val oldTrafficLights: List<TrafficLight>,
    private val newTrafficLights: List<TrafficLight>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldTrafficLights.size

    override fun getNewListSize(): Int = newTrafficLights.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldTrafficLights[oldItemPosition].id == newTrafficLights[newItemPosition].id

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldTrafficLights[oldItemPosition] == newTrafficLights[newItemPosition]
}