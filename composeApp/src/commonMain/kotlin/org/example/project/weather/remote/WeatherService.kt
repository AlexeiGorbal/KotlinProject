package org.example.project.weather.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class WeatherService(private val client: HttpClient) {
    suspend fun getWeather(locationId: String): WeatherEntity {
        return client.get("forecast.json") {
            parameter("days", 8)
            parameter("q", locationId)
        }.body()
    }
}