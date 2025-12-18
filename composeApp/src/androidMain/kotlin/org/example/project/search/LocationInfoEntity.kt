package org.example.project.search

import com.google.gson.annotations.SerializedName

data class LocationInfoEntity(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("region") val region: String,
    @SerializedName("country") val country: String,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)