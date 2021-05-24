package com.aliensquad.trafficlightmonitor.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aliensquad.trafficlightmonitor.core.R
import com.aliensquad.trafficlightmonitor.core.data.model.Intersection
import com.aliensquad.trafficlightmonitor.core.databinding.ItemIntersectionBinding
import com.aliensquad.trafficlightmonitor.core.utils.ImageConfiguration
import com.aliensquad.trafficlightmonitor.core.utils.ImageConfiguration.getTrafficLightStatusResource
import com.aliensquad.trafficlightmonitor.core.utils.IntersectionDiffCallback

class IntersectionAdapter :
    RecyclerView.Adapter<IntersectionAdapter.ViewHolder>() {

    private val intersections = mutableListOf<Intersection>()

    fun setTrafficLights(intersections: List<Intersection>) {
        val diffCallback = IntersectionDiffCallback(this.intersections, intersections)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.intersections.clear()
        this.intersections.addAll(intersections)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemIntersectionBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(intersections[position])

    override fun getItemCount(): Int = intersections.size

    inner class ViewHolder(private val binding: ItemIntersectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(intersection: Intersection) {
            val resource = when (intersection.currentStatus) {
                1 -> getTrafficLightStatusResource(ImageConfiguration.TrafficLightStatus.RED)
                2 -> getTrafficLightStatusResource(ImageConfiguration.TrafficLightStatus.YELLOW)
                else -> getTrafficLightStatusResource(ImageConfiguration.TrafficLightStatus.GREEN)
            }
            binding.tvInfo.apply {
                text = itemView.context.getString(
                    R.string.intersection_info,
                    intersection.name,
                    intersection.waitingTimeInSeconds
                )
                setCompoundDrawablesWithIntrinsicBounds(resource, 0, 0, 0)
            }
        }
    }
}