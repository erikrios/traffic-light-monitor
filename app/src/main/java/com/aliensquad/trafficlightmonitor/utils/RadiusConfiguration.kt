package com.aliensquad.trafficlightmonitor.utils

object RadiusConfiguration {

    enum class Radius(val distance: String) {
        KM_3("3 KM"),
        KM_5("5 KM"),
        KM_7("7 KM"),
        KM_9("9 KM"),
        KM_11("11 KM"),
        KM_13("13 KM"),
        KM_15("15 KM")
    }

    fun generateRadiusValues() = Radius.values().map { it.distance }

    fun getRadiusFromValue(value: String) = Radius.valueOf(value)
}