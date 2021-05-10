package com.aliensquad.trafficlightmonitor.utils

import androidx.recyclerview.widget.DiffUtil
import com.aliensquad.trafficlightmonitor.data.model.Intersection

class IntersectionDiffCallback(
    private val oldIntersection: List<Intersection>,
    private val newIntersection: List<Intersection>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldIntersection.size

    override fun getNewListSize(): Int = newIntersection.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldIntersection[oldItemPosition].name == newIntersection[newItemPosition].name

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldIntersection[oldItemPosition] == newIntersection[newItemPosition]
}