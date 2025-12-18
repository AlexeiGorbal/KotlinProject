package org.example.project.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DayWeatherEntity(
    @SerialName("date_epoch") val timestamp: Long,
    @SerialName("day") val day: DayEntity,
    @SerialName("hour") val hours: List<HourWeatherEntity>
) {

    @Serializable
    data class DayEntity(
        @SerialName("mintemp_f") val minTempF: Float,
        @SerialName("maxtemp_f") val maxTempF: Float,
        @SerialName("condition") val weatherState: WeatherStateEntity,
    )
}