package org.example.project.di

import org.koin.core.Koin
import org.koin.core.context.startKoin

lateinit var koin: Koin

fun initKoin() {
    koin = startKoin {
        modules(appModule)
    }.koin
}