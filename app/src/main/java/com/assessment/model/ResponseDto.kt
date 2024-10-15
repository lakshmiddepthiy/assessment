package com.assessment.model

import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class WeatherResponse(
    val main: Main,
    val visibility: String,
    val weather: List<Weather>
)

@JsonClass(generateAdapter = true)
data class Main(
    val humidity: String,
    val sea_level: String,
)

@JsonClass(generateAdapter = true)
data class Weather(
    val main: String,
    val description: String
)
