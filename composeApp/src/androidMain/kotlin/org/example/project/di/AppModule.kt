package org.example.project.di

import org.example.project.location.search.LocationSearchViewModel
import org.example.project.map.LocationWeatherViewModel
import org.example.project.web.NetworkModule
import org.koin.dsl.module

val appModule = module {
    single { NetworkModule() }
    single { get<NetworkModule>().provideRetrofit() }
    single { get<NetworkModule>().locationApi }
    single { get<NetworkModule>().weatherApi }

    factory {
        LocationWeatherViewModel(get(), get())
        LocationSearchViewModel(get())
    }
}