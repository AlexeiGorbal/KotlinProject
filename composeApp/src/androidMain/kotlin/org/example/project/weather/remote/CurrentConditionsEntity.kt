package org.example.project.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentConditionsEntity(
    @SerialName("temp_f") val tempF: Float,
    @SerialName("feelslike_f") val feelsLikeF: Float,
    @SerialName("condition") val weatherState: WeatherStateEntity
)