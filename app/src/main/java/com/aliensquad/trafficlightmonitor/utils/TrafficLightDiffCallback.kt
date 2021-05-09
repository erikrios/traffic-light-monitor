package com.aliensquad.trafficlightmonitor.utils

import androidx.recyclerview.widget.DiffUtil
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight

class TrafficLightDiffCallback : DiffUtil.ItemCallback<TrafficLight>() {

    override fun areItemsTheSame(oldItem: TrafficLight, newItem: TrafficLight): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: TrafficLight, newItem: TrafficLight): Boolean =
        oldItem == newItem
}