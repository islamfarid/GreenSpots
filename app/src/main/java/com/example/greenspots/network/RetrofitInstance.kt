package com.example.greenspots.network

import com.example.greenspots.plantandanimalspieces.SpeciesApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
object RetrofitInstance {

    private const val BASE_URL = "https://maps.googleapis.com/maps/api/"
    private const val SPECIES_API_BASE_URL = "https://your-species-api-url.com/"

    // Lazy initialization of Retrofit instance for Google Places API
    val api: GooglePlacesApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson converter
            .build()
            .create(GooglePlacesApi::class.java)
    }

    // Lazy initialization of Retrofit instance for Species API
    val speciesApi: SpeciesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(SPECIES_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) // Use Gson converter
            .build()
            .create(SpeciesApiService::class.java) // Correct class type
    }
}
