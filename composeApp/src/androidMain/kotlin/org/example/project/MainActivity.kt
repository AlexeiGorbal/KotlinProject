package org.example.project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.example.project.di.initKoin
import org.example.project.map.MapScreen
import org.example.project.viewmodel.LocationWeatherViewModel
import org.example.project.weather.LocationWeatherScreen
import org.koin.compose.viewmodel.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        initKoin()

        setContent {
            val viewModel: LocationWeatherViewModel = koinViewModel()
            val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()

            LocationWeatherScreen(
                locationId = selectedLocation?.name ?: "Minsk",
                content = {
                    MapScreen()
                }
            )
        }
    }
}