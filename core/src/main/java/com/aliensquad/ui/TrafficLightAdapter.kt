package com.aliensquad.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliensquad.trafficlightmonitor.core.data.model.TrafficLight
import com.aliensquad.trafficlightmonitor.core.databinding.ItemTrafficLightBinding
import com.aliensquad.utils.TrafficLightDiffCallback

class TrafficLightAdapter(private val onClickListener: ((TrafficLight) -> Unit)) :
    RecyclerView.Adapter<TrafficLightAdapter.ViewHolder>() {

    private val trafficLights = mutableListOf<TrafficLight>()

    fun setTrafficLights(trafficLights: List<TrafficLight>) {
        val diffCallback = TrafficLightDiffCallback(this.trafficLights, trafficLights)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.trafficLights.clear()
        this.trafficLights.addAll(trafficLights)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemTrafficLightBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(trafficLights[position], onClickListener)

    override fun getItemCount(): Int = trafficLights.size

    inner class ViewHolder(private val binding: ItemTrafficLightBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(trafficLight: TrafficLight, clickListener: ((TrafficLight) -> Unit)) {
            binding.apply {
                tvName.text = trafficLight.name
                tvAddress.text = trafficLight.address
            }
            itemView.setOnClickListener { clickListener(trafficLight) }
        }
    }
}