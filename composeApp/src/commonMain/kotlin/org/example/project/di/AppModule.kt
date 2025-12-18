package org.example.project.di

import org.example.project.location.remote.LocationService
import org.example.project.viewmodel.LocationSearchViewModel
import org.example.project.viewmodel.LocationWeatherViewModel
import org.example.project.weather.remote.WeatherService
import org.example.project.web.NetworkModule
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { NetworkModule().httpClient }
    single { WeatherService(get()) }
    single { LocationService(get()) }

    viewModel { LocationWeatherViewModel(get(), get()) }
    viewModel { LocationSearchViewModel(get()) }
}