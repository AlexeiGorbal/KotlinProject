package org.example.project.local

import kotlinx.serialization.Serializable

@Serializable
data class LocationInfo(
    val id: Long,
    val name: String,
    val region: String,
    val country: String,
    val lat: Double,
    val lon: Double
)