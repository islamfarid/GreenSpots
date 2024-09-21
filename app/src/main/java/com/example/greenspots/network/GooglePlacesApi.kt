package com.example.greenspots.network

import com.example.greenspots.details.model.PlaceDetailsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {

    @GET("place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") apiKey: String
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
