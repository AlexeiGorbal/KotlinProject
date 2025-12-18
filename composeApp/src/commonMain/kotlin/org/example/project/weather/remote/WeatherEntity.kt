package org.example.project.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherEntity(
    @SerialName("location") val location: ForecastLocationEntity,
    @SerialName("current") val current: CurrentConditionsEntity,
    @SerialName("forecast") val forecast: ForecastEntity,
) {

    @Serializable
    data class ForecastEntity(
        @SerialName("forecastday") val days: List<DayWeatherEntity>
    )
}