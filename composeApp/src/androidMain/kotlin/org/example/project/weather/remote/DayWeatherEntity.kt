package org.example.project.weather.remote

import com.google.gson.annotations.SerializedName

data class DayWeatherEntity(
    @SerializedName("date_epoch") val timestamp: Long,
    @SerializedName("day") val day: DayEntity,
    @SerializedName("hour") val hours: List<HourWeatherEntity>
) {

    data class DayEntity(
        @SerializedName("mintemp_f") val minTempF: Float,
        @SerializedName("maxtemp_f") val maxTempF: Float,
        @SerializedName("condition") val weatherState: WeatherStateEntity,
    )
}