package org.example.project.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.viewmodel.LocationSearchUiState
import org.example.project.viewmodel.LocationSearchViewModel
import org.example.project.viewmodel.LocationWeatherViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LocationSearchScreen(
    onNavToMapScreen: (String) -> Unit,
    viewModel: LocationSearchViewModel = koinViewModel(),
    weatherViewModel: LocationWeatherViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val text by viewModel.text.collectAsStateWithLifecycle()

    Column(modifier = Modifier.padding(top = 50.dp)) {
        OutlinedTextField(
            value = text,
            onValueChange = { viewModel.searchLocations(it) },
            modifier = Modifier
                .height(50.dp)
                .fillMaxWidth()
        )

        when (val state = uiState) {
            is LocationSearchUiState.Initial -> {}
            is LocationSearchUiState.Loading -> {}
            is LocationSearchUiState.NoResults -> {}
            is LocationSearchUiState.Suggestions -> {
                LazyColumn() {
                    items(state.locations) { location ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .height(30.dp)
                                .fillMaxWidth()
                                .background(Color.Blue)
                                .clickable {
                                    weatherViewModel.onLocationSelected(location)
                                    onNavToMapScreen(location.name)
                                }
                        ) {
                            Text(text = location.name, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}