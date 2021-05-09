package com.aliensquad.trafficlightmonitor.data.model

import com.google.gson.annotations.SerializedName

data class Result<T>(
    @SerializedName("status")
    val status: String,
    @SerializedName("message")
    val message: String? = null,
    @SerializedName("data")
    val data: T? = null
)
