package org.example.project.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HourWeatherEntity(
    @SerialName("time_epoch") val timestamp: Long,
    @SerialName("temp_f") val tempF: Float,
    @SerialName("condition") val weatherState: WeatherStateEntity
)