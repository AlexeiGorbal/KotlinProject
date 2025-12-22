package org.example.project.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.example.project.local.FirebaseLocationService
import org.example.project.local.LocationInfo
import org.example.project.location.remote.LocationInfoEntity
import org.example.project.location.remote.LocationService
import org.example.project.weather.remote.WeatherEntity
import org.example.project.weather.remote.WeatherService

class LocationWeatherViewModel(
    private val locationService: LocationService,
    private val weatherService: WeatherService,
    private val firebaseService: FirebaseLocationService,
) : ViewModel() {

    private val _selectedLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val selectedLocation = _selectedLocation.asStateFlow()

    private val _userLocation = MutableStateFlow<LocationInfoEntity?>(null)
    val userLocation = _userLocation.asStateFlow()

    private val _uiState = MutableStateFlow<WeatherEntity?>(null)
    val uiState = _uiState.asStateFlow()

    val savedLocations: Flow<List<LocationInfo>>
        get() = firebaseService.getLocations()

    private val _isLocationSaved = MutableStateFlow<Boolean?>(null)
    val isLocationSaved = _isLocationSaved.asStateFlow()

    fun removeLocation(index: Int) {
        viewModelScope.launch {
            val list = savedLocations.firstOrNull() ?: return@launch
            firebaseService.removeLocation(list[index].id)
        }
    }

    fun loadWeather(locationId: String) {
        viewModelScope.launch {
            val weather = weatherService.getWeather(locationId)
            _uiState.value = weather
        }
    }

    fun onUserLocationAvailable(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location =
                locationService.getLocationByCoordinates("$lat,$lon").firstOrNull() ?: return@launch
            _userLocation.value = location
            _selectedLocation.value = location

            val savedLocation = firebaseService.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
        }
    }

    fun onLocationSelected(location: LocationInfoEntity) {
        viewModelScope.launch {
            _selectedLocation.value = location

            val savedLocation = firebaseService.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
        }
    }

    fun onLocationSelectedOnMap(lat: Double, lon: Double) {
        viewModelScope.launch {
            val location =
                locationService.getLocationByCoordinates("$lat,$lon").firstOrNull() ?: return@launch
            _selectedLocation.value = location

            val savedLocation = firebaseService.getLocationById(location.id)
            _isLocationSaved.value = savedLocation != null
        }
    }

    fun onSelectedLocationSavedStateChanged() {
        viewModelScope.launch {
            val selectedLocation = _selectedLocation.value ?: return@launch
            val savedLocation = firebaseService.getLocationById(selectedLocation.id)
            if (savedLocation == null) {
                firebaseService.addLocation(selectedLocation.toModel())
                _isLocationSaved.value = true
            } else {
                firebaseService.removeLocation(selectedLocation.id)
                _isLocationSaved.value = false
            }
        }
    }

    fun onLocationDeselected() {
        _selectedLocation.value = null
    }

    private val _searchUiState =
        MutableStateFlow<LocationSearchUiState>(LocationSearchUiState.Initial)
    val searchUiState = _searchUiState.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    fun searchLocations(userInput: String) {
        _text.value = userInput

        if (userInput.isBlank()) {
            _searchUiState.value = LocationSearchUiState.NoResults
            return
        }

        _searchUiState.value = LocationSearchUiState.Loading

        viewModelScope.launch {
            val result = locationService.getLocationsByInput(userInput)
            _searchUiState.value = if (result.isEmpty()) {
                LocationSearchUiState.NoResults
            } else {
                LocationSearchUiState.Suggestions(result)
            }
        }
    }
}

fun LocationInfoEntity.toModel(): LocationInfo {
    return LocationInfo(
        id = id ?: 1,
        name,
        region,
        country,
        lat,
        lon
    )
}

fun LocationInfo.toEntity(): LocationInfoEntity {
    return LocationInfoEntity(
        id = id ?: 1,
        name,
        region,
        country,
        lat,
        lon
    )
}