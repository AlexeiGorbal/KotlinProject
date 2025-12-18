package org.example.project.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherStateEntity(
    @SerialName("text") val text: String,
    @SerialName("icon") val icon: String
)