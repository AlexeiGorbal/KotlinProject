package org.example.project.weather

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.example.project.viewmodel.LocationWeatherViewModel
import org.example.project.weather.remote.DayWeatherEntity
import org.example.project.weather.remote.HourWeatherEntity
import org.example.project.weather.remote.WeatherEntity
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationWeatherScreen(
    locationId: String,
    content: @Composable () -> Unit,
    viewModel: LocationWeatherViewModel = koinViewModel()
) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(locationId) {
        if (locationId.isNotEmpty()) viewModel.loadWeather(locationId)
    }

    uiState?.let { weather ->
        BottomSheetScaffold(
            modifier = Modifier.fillMaxHeight(),
            scaffoldState = scaffoldState,
            sheetPeekHeight = if (locationId.isEmpty()) 0.dp else 100.dp,
            sheetContent = {
                Column(modifier = Modifier.fillMaxSize()) {
                    CurrentConditionsItem(weather)

                    Text(
                        text = "${weather.location.region} ${weather.location.country}",
                    )

                    val day = weather.forecast.days.first()
                    HourlyForecastRow(day.hours)

                    LazyColumn() {
                        items(weather.forecast.days) { dayWeather ->
                            ForecastWeatherItem(dayWeather)
                        }
                    }
                }
            }
        ) {
            content()
        }
    }
}

@Composable
fun CurrentConditionsItem(weather: WeatherEntity) {
    Row(modifier = Modifier.fillMaxWidth()) {
        AsyncImage(
            model = "https:${weather.current.weatherState.icon}",
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Text(
            text = weather.current.tempF.toString(),
            fontSize = 40.sp,
            modifier = Modifier.padding(16.dp)
        )

        Column() {
            Text(
                text = weather.current.weatherState.text,
                modifier = Modifier.padding(16.dp)
            )

            Text(
                text = "Fells like ${weather.current.feelsLikeF}",
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun ForecastWeatherItem(dayWeather: DayWeatherEntity) {
    var isOpen by remember { mutableStateOf(false) }

    Column() {
        Row(modifier = Modifier.clickable { isOpen = !isOpen }) {
            AsyncImage(
                model = "https:${dayWeather.day.weatherState.icon}",
                contentDescription = null,
                modifier = Modifier.size(100.dp)
            )

            Column() {
                Text(text = "${dayWeather.timestamp}")

                Text(text = dayWeather.day.weatherState.text)
            }

            Row() {
                Text(text = "${dayWeather.day.maxTempF}")
                Text(text = "${dayWeather.day.minTempF}")
            }

        }
        if (isOpen) HourlyForecastRow(dayWeather.hours)
    }
}

@Composable
fun HourlyForecastRow(hourlyForecast: List<HourWeatherEntity>) {
    LazyRow() {
        items(hourlyForecast) { hourWeather ->
            HourWeatherItem(hourWeather)
        }
    }
}

@Composable
fun HourWeatherItem(hourWeather: HourWeatherEntity) {
    Column() {
        Text(text = "${hourWeather.timestamp}")

        AsyncImage(
            model = "https:${hourWeather.weatherState.icon}",
            contentDescription = null,
            modifier = Modifier.size(100.dp)
        )

        Text(text = "${hourWeather.tempF}")
    }
}