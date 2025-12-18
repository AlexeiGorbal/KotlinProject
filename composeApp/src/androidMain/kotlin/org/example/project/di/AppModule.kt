package org.example.project.di

import org.example.project.location.search.LocationSearchViewModel
import org.example.project.location.search.remote.LocationService
import org.example.project.map.LocationWeatherViewModel
import org.example.project.weather.remote.WeatherService
import org.example.project.web.NetworkModule
import org.koin.dsl.module

val appModule = module {
    single { NetworkModule().httpClient }
    single { WeatherService(get()) }
    single { LocationService(get()) }

    factory { LocationWeatherViewModel(get(), get()) }
    factory { LocationSearchViewModel(get()) }
}