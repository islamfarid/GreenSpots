package com.example.greenspots.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String, // lat,lng format
        @Query("radius") radius: Int, // Search radius in meters
        @Query("type") type: String = "park", // Type of place to search for
        @Query("key") apiKey: String // Your API Key
    ): PlacesResponse
}
