package org.example.project.weather.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("forecast.json?days=8")
    suspend fun getWeather(@Query("q") locationId: String): WeatherEntity
}