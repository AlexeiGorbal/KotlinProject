package org.example.project.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.location.search.remote.LocationApi
import org.example.project.location.search.remote.LocationInfoEntity
import org.example.project.weather.remote.WeatherApi
import org.example.project.weather.remote.WeatherEntity

class LocationWeatherViewModel(
    private val locationApi: LocationApi,
    private val weatherApi: WeatherApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherEntity?>(null)
    val uiState = _uiState.asStateFlow()

    private val _selectedLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    fun loadWeather(locationId: Long) {
        viewModelScope.launch {
            _uiState.value = weatherApi.getWeather("London")
        }
    }

    fun onLocationSelectedOnMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location = locationApi.getLocationByCoordinates("$lat,$lon")
            _selectedLocation.value = location.firstOrNull()
        }
    }
}