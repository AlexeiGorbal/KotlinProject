package org.example.project.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.location.search.remote.LocationInfoEntity
import org.example.project.location.search.remote.LocationService
import org.example.project.weather.remote.WeatherEntity
import org.example.project.weather.remote.WeatherService

class LocationWeatherViewModel(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
) : ViewModel() {

    private val _uiState = MutableStateFlow<WeatherEntity?>(null)
    val uiState = _uiState.asStateFlow()

    private val _selectedLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _userLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val userLocation = _userLocation.asStateFlow()

    fun loadWeather(locationId: Long) {
        viewModelScope.launch {
            _uiState.value = weatherService.getWeather("London")
        }
    }

    fun onLocationSelectedOnMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location = locationService.getLocationByCoordinates("$lat,$lon")
            _selectedLocation.value = location.firstOrNull()
        }
    }

    fun onUserLocationAvailable(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location = locationService.getLocationByCoordinates("$lat,$lon").firstOrNull()
            _userLocation.value = location
            _selectedLocation.value = location

            /*val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null*/
        }
    }
}