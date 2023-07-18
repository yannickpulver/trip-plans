package com.yannickpulver.tripplans.ui.feature.plans

import kotlin.random.Random

val antarcticaLocations = listOf(
    "McMurdo Station",
    "Amundsen–Scott South Pole Station",
    "Rothera Research Station",
    "Halley Research Station",
    "Scott Base",
    "Casey Station",
    "Davis Station",
    "Mawson Station",
    "Vostok Station",
    "Troll Station",
    "Concordia Station",
    "Neumayer-Station III",
    "Princess Elisabeth Antarctica"
)

val arcticLocations = listOf(
    "Alert, Nunavut",
    "Ny-Ålesund",
    "Longyearbyen",
    "Barrow, Alaska",
    "Thule Air Base",
    "Resolute, Nunavut",
    "Tiksi, Russia",
    "Vorkuta, Russia",
    "Norilsk, Russia",
    "Yellowknife, Canada",
    "Iqaluit, Nunavut",
    "Nuuk, Greenland",
    "Tromsø, Norway"
)

fun getRandomLocation(): String {
    // Decide whether to pick from Antarctica (0) or Arctic (1)
    val region = Random.nextInt(2)

    return if (region == 0) {
        antarcticaLocations.random()
    } else {
        arcticLocations.random()
    }
}