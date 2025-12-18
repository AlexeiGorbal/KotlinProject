package org.example.project.weather.remote

import com.google.gson.annotations.SerializedName

data class WeatherEntity(
    @SerializedName("location") val location: ForecastLocationEntity,
    @SerializedName("current") val current: CurrentConditionsEntity,
    @SerializedName("forecast") val forecast: ForecastEntity,
) {

    data class ForecastEntity(
        @SerializedName("forecastday") val days: List<DayWeatherEntity>
    )
}