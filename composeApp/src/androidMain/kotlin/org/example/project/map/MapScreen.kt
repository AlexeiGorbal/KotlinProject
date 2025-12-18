package org.example.project.map

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
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
import org.example.project.location.remote.LocationInfoEntity
import org.example.project.viewmodel.LocationWeatherViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MapScreen(
    viewModel: LocationWeatherViewModel = koinViewModel()
) {
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }
    val markerState = remember { MarkerState() }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedLocation by viewModel.selectedLocation.collectAsStateWithLifecycle()
    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()

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

    LaunchedEffect(Unit) {
        viewModel.loadWeather(322)
    }

    Box {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = rememberCameraPositionState {
                currentLocation?.let { position = CameraPosition.fromLatLngZoom(it, 10f) }
            },
            onMapLongClick = {
                viewModel.onLocationSelectedOnMap(lat = it.latitude, lon = it.longitude)

                val location = LatLng(it.latitude, it.longitude)
                currentLocation = location
                markerState.position = location
            }
        ) {
            Marker(state = markerState)
        }

        Button(
            onClick = {
                permissionLauncher.launch(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION))
            },
            modifier = Modifier.padding(top = 70.dp)
        ) {
            Text("Найти меня")
        }

        userLocation?.let { PartialBottomSheet(it) }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartialBottomSheet(
    selectedLocation: LocationInfoEntity
) {
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 100.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(
            onClick = { showBottomSheet = true }
        ) {
            Text("Display partial bottom sheet")
        }

        if (showBottomSheet) {
            ModalBottomSheet(
                modifier = Modifier.fillMaxHeight(),
                sheetState = sheetState,
                onDismissRequest = { showBottomSheet = false }
            ) {
                Text(
                    selectedLocation.country,
                    modifier = Modifier.padding(16.dp)
                )

                Text(
                    selectedLocation.name,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}