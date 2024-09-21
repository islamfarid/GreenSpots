package com.example.greenspots.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"

    // Lazy initialization of Retrofit instance
    val api: GooglePlacesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson converter
            .build()
            .create(GooglePlacesApi::class.java)
    }
}
