package org.example.project.weather.remote

import com.google.gson.annotations.SerializedName

data class CurrentConditionsEntity(
    @SerializedName("temp_f") val tempF: Float,
    @SerializedName("feelslike_f") val feelsLikeF: Float,
    @SerializedName("condition") val weatherState: WeatherStateEntity
)