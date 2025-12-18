package org.example.project.weather.remote

import com.google.gson.annotations.SerializedName

data class HourWeatherEntity(
    @SerializedName("time_epoch") val timestamp: Long,
    @SerializedName("temp_f") val tempF: Float,
    @SerializedName("condition") val weatherState: WeatherStateEntity
)