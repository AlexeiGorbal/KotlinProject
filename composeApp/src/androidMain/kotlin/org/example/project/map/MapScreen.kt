package org.example.project.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import org.example.project.viewmodel.LocationWeatherViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    viewModel: LocationWeatherViewModel = koinViewModel()
) {
    val markerState = remember { MarkerState() }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient((context))
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if (it[Manifest.permission.ACCESS_COARSE_LOCATION] == true) {
            onLocationPermissionGranted(
                fusedLocationClient = fusedLocationClient,
                onUserLocationAvailable = { location ->
                    viewModel.onUserLocationAvailable(location.latitude, location.longitude)
                }
            )
        }
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { LatLng(it.lat, it.lon) }?.also { latLng ->
            markerState.position = latLng
        }
    }

    LaunchedEffect(selectedLocation) {
        selectedLocation?.let { LatLng(it.lat, it.lon) }?.also { latLng ->
            markerState.position = latLng
        }
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                currentLocation?.let { position = CameraPosition.fromLatLngZoom(it, 100f) }
            },
            onMapLongClick = {
                viewModel.onLocationSelectedOnMap(lat = it.latitude, lon = it.longitude)

                val location = LatLng(it.latitude, it.longitude)
                currentLocation = location
                markerState.position = location
            }
        ) {
            if(selectedLocation!=null) Marker(state = markerState)
        }

        Button(
            onClick = {
                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
            },
            modifier = Modifier.padding(top = 70.dp)
        ) { Text("Найти меня") }
    }
}

@SuppressLint("MissingPermission")
fun onLocationPermissionGranted(
    fusedLocationClient: FusedLocationProviderClient,
    onUserLocationAvailable: (Location) -> Unit
) {
    fusedLocationClient
        .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, null)
        .addOnSuccessListener {
            if (it != null) {
                onUserLocationAvailable(it)
            }
        }
}