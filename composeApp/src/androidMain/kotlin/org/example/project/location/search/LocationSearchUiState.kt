package org.example.project.location.search

import org.example.project.location.search.remote.LocationInfoEntity

sealed class LocationSearchUiState {

    data object Initial : LocationSearchUiState()

    data object Loading : LocationSearchUiState()

    data class Suggestions(val locations: List<LocationInfoEntity>) : LocationSearchUiState()

    data object NoResults : LocationSearchUiState()
}