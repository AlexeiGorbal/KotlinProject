package org.example.project.web

import okhttp3.OkHttpClient
import org.example.project.search.LocationApi
import org.example.project.weather.remote.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NetworkModule {

    val locationApi: LocationApi by lazy {
        provideRetrofit().create(LocationApi::class.java)
    }

    val weatherApi: WeatherApi by lazy {
        provideRetrofit().create(WeatherApi::class.java)
    }

    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/v1/")
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(ApiKeyInterceptor())
                    .build()
            )
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}