package com.aliensquad.trafficlightmonitor.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Intersection(
    @SerializedName("name")
    val name: String,
    @SerializedName("waitingTimeInSeconds")
    val waitingTimeInSeconds: Long,
    @SerializedName("currentStatus")
    val currentStatus: String
) : Parcelable
