package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.location.remote.LocationInfoEntity
import org.example.project.location.remote.LocationService
import org.example.project.weather.remote.WeatherEntity
import org.example.project.weather.remote.WeatherService

class LocationWeatherViewModel(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
) : ViewModel() {

    /*private val _uiState = MutableStateFlow<WeatherEntity?>(null)
    val uiState = _uiState.asStateFlow()

    private val _selectedLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _userLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val userLocation = _userLocation.asStateFlow()

    fun loadWeather(locationId: String) {
        viewModelScope.launch {
            _uiState.value = weatherService.getWeather(locationId)
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

            *//*val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null*//*
        }
    }*/

    private val _selectedLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _userLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val _uiState = MutableStateFlow<WeatherEntity?>(null)
    val uiState = _uiState.asStateFlow()

    /*val locations: Flow<List<LocationInfo>>
        get() = savedLocationsRepository.getLocations()*/

    private val _isLocationSaved = MutableStateFlow<Boolean?>(null)
    val isLocationSaved = _isLocationSaved.asStateFlow()

    fun loadWeather(locationId: String) {
        viewModelScope.launch {
            val weather = weatherService.getWeather(locationId)
            _uiState.value = weather
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

    fun onLocationSelected(location: LocationInfoEntity) {
        viewModelScope.launch {
            _selectedLocation.value = location

            /*val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null*/
        }
    }

    fun onLocationSelectedOnMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location = locationService.getLocationByCoordinates("$lat,$lon").firstOrNull()
            _selectedLocation.value = location

            /*val savedLocation = savedLocationsRepository.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null*/
        }
    }

    /*fun onSelectedLocationSavedStateChanged() {
        viewModelScope.launch {
            val selectedLocation = _selectedLocation.value ?: return@launch
            val savedLocation = savedLocationsRepository.getLocationById(selectedLocation.id)
            if (savedLocation == null) {
                savedLocationsRepository.addLocation(selectedLocation)
                _isLocationSaved.value = true
            } else {
                savedLocationsRepository.removeLocation(selectedLocation)
                _isLocationSaved.value = false
            }
        }
    }*/

    fun onLocationDeselected() {
        _selectedLocation.value = null
    }
}