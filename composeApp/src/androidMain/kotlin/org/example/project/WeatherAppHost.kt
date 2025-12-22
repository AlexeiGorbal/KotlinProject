package org.example.project

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.example.project.map.MapScreen
import org.example.project.saved.SavedLocationScreen
import org.example.project.search.LocationSearchScreen
import org.example.project.viewmodel.LocationWeatherViewModel
import org.example.project.weather.LocationWeatherScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WeatherAppHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val sharedViewModel: LocationWeatherViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = MapRoute("Minsk"),
        modifier = modifier
    ) {
        composable<MapRoute> { navBackStackEntry ->
            val param = navBackStackEntry.toRoute<MapRoute>()

            LocationWeatherScreen(
                locationId = param.locationId,
                viewModel = sharedViewModel,
                content = {
                    MapScreen(
                        onNavToSearchScreen = {
                            navController.navigate(LocationSearchRoute)
                        },
                        onNavToSavedScreen = {
                            navController.navigate(SavedLocationRoute)
                        },
                        viewModel = sharedViewModel
                    )
                }
            )
        }

        composable<LocationSearchRoute> {
            LocationSearchScreen(
                onNavToMapScreen = { locationId ->
                    navController.navigate(MapRoute(locationId = locationId))
                },
                weatherViewModel = sharedViewModel
            )
        }

        composable<SavedLocationRoute> {
            SavedLocationScreen(
                onNavToMapScreen = { locationId ->
                    navController.navigate(MapRoute(locationId = locationId))
                },
                viewModel = sharedViewModel
            )
        }
    }
}

@Serializable
data class MapRoute(val locationId: String)

@Serializable
object LocationSearchRoute

@Serializable
object SavedLocationRoute