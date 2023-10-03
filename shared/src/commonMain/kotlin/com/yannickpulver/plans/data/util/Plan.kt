package com.yannickpulver.plans.data.util

fun randomEmoji(): String {
    val emojis = listOf(
        "🌄", "🌅", "🌇", "🌈", "🌉", "🌌", "🌠",
        "⭐", "🌟", "🌞", "🌝", "🌚", "🌛",
        "🌜", "🌙", "🪐", "⛅", "🌤", "🌥",
    )
    return emojis.random()
}

fun randomPastelColor(): String {
    val colors = listOf(
        "#6096B4",
        "#93BFCF",
        "#98FB98",
        "#BDCDD6",
        "#EEE9DA",
        "#F8EDE3",
        "#DFD3C3",
        "#D0B8A8",
        "#A7727D",
        "#ABC4AA"
    )
    return colors.random()
}