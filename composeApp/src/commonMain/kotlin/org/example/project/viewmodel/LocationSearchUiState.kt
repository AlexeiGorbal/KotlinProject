package org.example.project.viewmodel

import org.example.project.location.remote.LocationInfoEntity

sealed class LocationSearchUiState {

    data object Initial : LocationSearchUiState()

    data object Loading : LocationSearchUiState()

    data class Suggestions(val locations: List<LocationInfoEntity>) : LocationSearchUiState()

    data object NoResults : LocationSearchUiState()
}