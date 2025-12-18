package org.example.project.location.search.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("search.json?")
    suspend fun getLocationsByInput(@Query("q") input: String): List<LocationInfoEntity>

    @GET("search.json?")
    suspend fun getLocationByCoordinates(@Query("q") latLng: String): List<LocationInfoEntity>
}