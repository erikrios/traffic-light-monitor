package com.aliensquad.trafficlightmonitor.utils

object RadiusConfiguration {

    enum class Radius(val distance: Int) {
        KM_3(3),
        KM_5(5),
        KM_7(7),
        KM_9(9),
        KM_11(11),
        KM_13(13),
        KM_15(15)
    }

    fun generateRadiusValues() = Radius.values().map { it.distance.toString().plus(" KM") }

    fun getRadiusFromIndex(index: Int) = Radius.values()[index]
}