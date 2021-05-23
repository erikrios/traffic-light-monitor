package com.aliensquad.trafficlightmonitor.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Intersection(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("latitude")
    val latitude: Double,
    @SerializedName("longitude")
    val longitude: Double,
    @SerializedName("waitingTimeInSeconds")
    val waitingTimeInSeconds: Long,
    @SerializedName("currentStatus")
    val currentStatus: Int
) : Parcelable
