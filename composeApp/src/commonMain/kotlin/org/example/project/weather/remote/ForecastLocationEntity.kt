package org.example.project.weather.remote

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ForecastLocationEntity(
    @SerialName("region") val region: String,
    @SerialName("country") val country: String
)