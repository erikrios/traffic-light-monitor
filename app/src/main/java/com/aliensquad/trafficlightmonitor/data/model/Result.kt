package com.aliensquad.trafficlightmonitor.data.model

data class Result<T>(
    val status: String,
    val message: String? = null,
    val data: T? = null
)
