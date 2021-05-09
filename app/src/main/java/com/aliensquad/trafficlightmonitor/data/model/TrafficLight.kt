package com.aliensquad.trafficlightmonitor.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrafficLight(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("address")
    val address: String,
    @SerializedName("vehiclesDensityInMinutes")
    val vehiclesDensityInMinutes: Int? = null,
    @SerializedName("intersections")
    val intersections: List<Intersection>? = null
) : Parcelable