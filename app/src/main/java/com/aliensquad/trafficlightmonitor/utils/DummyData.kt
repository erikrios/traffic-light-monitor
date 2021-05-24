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
                -8.0181039 + "0.0$i".toDouble(),
                111.4672751 + "0.0$i".toDouble(),
                39 + i,
                listOf(
                    Intersection(
                        (i + 0).toLong(),
                        "Persimpangan 1",
                        -8.909560,
                        111.569271,
                        19,
                        1
                    ),
                    Intersection(
                        (i + 1).toLong(),
                        "Persimpangan 2",
                        -8.909660,
                        111.569471,
                        33,
                        1
                    ),
                    Intersection(
                        (i + 2).toLong(),
                        "Persimpangan 3",
                        -8.909760,
                        111.570271,
                        25,
                        2
                    ),
                    Intersection(
                        (i + 3).toLong(),
                        "Persimpangan 4",
                        -8.909860,
                        111.5703271,
                        16,
                        3
                    ),
                ),
                0
            )
            trafficLights.add(trafficLight)
        }

        return trafficLights
    }

    fun getTrafficLight(id: Long) = generateTrafficLights().find { it.id == id }
}