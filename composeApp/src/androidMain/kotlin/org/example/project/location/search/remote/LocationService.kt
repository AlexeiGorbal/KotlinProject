package org.example.project.location.search.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class LocationService(private val client: HttpClient) {
    suspend fun getLocationsByInput(input: String): List<LocationInfoEntity> {
        return client.get("search.json") {
            parameter("q", input)
        }.body()
    }

    suspend fun getLocationByCoordinates(latLng: String): List<LocationInfoEntity> {
        return client.get("search.json") {
            parameter("q", latLng)
        }.body()
    }
}