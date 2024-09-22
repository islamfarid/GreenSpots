package com.example.greenspots.network

import com.example.greenspots.details.model.PlaceDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,  // LatLng as "lat,lng"
        @Query("radius") radius: Int,         // Search radius in meters
        @Query("type") type: String = "",     // Category type (e.g., "park", "lake"), default to an empty string
        @Query("key") apiKey: String          // Your API key
    ): PlacesResponse

    @GET("place/details/json")
    suspend fun getPlaceDetails(
        @Query("place_id") placeId: String,  // Place ID from previous API response
        @Query("key") apiKey: String
    ): PlaceDetailsResponse

    @GET("place/textsearch/json")
    suspend fun searchPlaces(
        @Query("query") query: String,
        @Query("key") apiKey: String
    ): PlacesResponse
}
