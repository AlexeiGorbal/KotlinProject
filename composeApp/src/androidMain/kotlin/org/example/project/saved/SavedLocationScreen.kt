package org.example.project.saved

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.viewmodel.LocationWeatherViewModel
import org.example.project.viewmodel.toEntity
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SavedLocationScreen(
    onNavToMapScreen: (String) -> Unit,
    viewModel: LocationWeatherViewModel = koinViewModel()
) {
    val savedLocations by viewModel.savedLocations.collectAsStateWithLifecycle(emptyList())

    Column() {
        Text(text = "Saved Locations")

        LazyColumn() {
            items(savedLocations) {location->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .height(30.dp)
                        .fillMaxWidth()
                        .background(Color.Blue)
                        .clickable {
                            viewModel.onLocationSelected(location.toEntity())
                            onNavToMapScreen(location.name)
                        }
                ) {
                    Text(text = location.name, color = Color.White)
                }
            }
        }
    }
}