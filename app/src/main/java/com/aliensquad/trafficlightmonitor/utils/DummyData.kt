package com.aliensquad.trafficlightmonitor.utils

import com.aliensquad.trafficlightmonitor.data.model.Intersection
import com.aliensquad.trafficlightmonitor.data.model.TrafficLight

object DummyData {

    fun generateTrafficLights(): List<TrafficLight> {
        val trafficLights = mutableListOf<TrafficLight>()

        for (i in 0 until 20) {
            val trafficLight = TrafficLight(
                i.toLong(),
                "Lampu Merah Pos Mlilir $i",
                "$i Jl. Raya Ponorogo - Madiun, Tenggang, Ngrupit, Kec. Jenangan, Kabupaten Ponorogo, Jawa Timur, 63492",
                39 + i,
                listOf(
                    Intersection("Persimpangan 1", 25, "RED"),
                    Intersection("Persimpangan 2", 22, "RED"),
                    Intersection("Persimpangan 3", 19, "GREEN"),
                    Intersection("Persimpangan 4", 31, "YELLOW"),
                )
            )
            trafficLights.add(trafficLight)
        }

        return trafficLights
    }
}