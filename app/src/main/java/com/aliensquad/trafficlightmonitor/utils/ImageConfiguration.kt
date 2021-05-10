package com.aliensquad.trafficlightmonitor.utils

import com.aliensquad.trafficlightmonitor.R

object ImageConfiguration {

    enum class TrafficLightWallpaper(val resource: Int) {
        TRAFFIC_LIGHT_1(R.drawable.traffic_light_1),
        TRAFFIC_LIGHT_2(R.drawable.traffic_light_2),
        TRAFFIC_LIGHT_3(R.drawable.traffic_light_3),
        TRAFFIC_LIGHT_4(R.drawable.traffic_light_4),
        TRAFFIC_LIGHT_5(R.drawable.traffic_light_5),
        TRAFFIC_LIGHT_6(R.drawable.traffic_light_6),
        TRAFFIC_LIGHT_7(R.drawable.traffic_light_7),
        TRAFFIC_LIGHT_8(R.drawable.traffic_light_8),
        TRAFFIC_LIGHT_9(R.drawable.traffic_light_9),
        TRAFFIC_LIGHT_10(R.drawable.traffic_light_10)
    }

    enum class TrafficLightStatus(val resource: Int) {
        YELLOW(R.drawable.bg_traffic_light_yellow),
        RED(R.drawable.bg_traffic_light_red),
        GREEN(R.drawable.bg_traffic_light_green),
    }

    fun getRandomTrafficLightWallpaperResource() = TrafficLightWallpaper.values().random().resource

    fun getTrafficLightStatusResource(status: TrafficLightStatus): Int = status.resource
}