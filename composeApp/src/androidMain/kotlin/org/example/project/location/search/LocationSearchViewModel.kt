package org.example.project.location.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.example.project.location.search.remote.LocationApi

class LocationSearchViewModel(
    private val api: LocationApi
) : ViewModel() {

    private val _uiState = MutableStateFlow<LocationSearchUiState>(LocationSearchUiState.Initial)
    val uiState = _uiState.asStateFlow()

    private val _text = MutableStateFlow("")
    val text = _text.asStateFlow()

    fun searchLocations(userInput: String) {
        _text.value = userInput

        if (userInput.isBlank()) {
            _uiState.value = LocationSearchUiState.NoResults
            return
        }

        _uiState.value = LocationSearchUiState.Loading

        viewModelScope.launch {
            val result = api.getLocationsByInput(userInput)
            _uiState.value = if (result.isEmpty()) {
                LocationSearchUiState.NoResults
            } else {
                LocationSearchUiState.Suggestions(result)
            }
        }
    }
}