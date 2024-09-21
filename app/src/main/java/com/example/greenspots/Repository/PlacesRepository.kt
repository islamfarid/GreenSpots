package com.example.greenspots.Repository

import android.util.Log
import com.example.greenspots.BuildConfig
import com.example.greenspots.details.model.PlaceDetailsResult
import com.example.greenspots.map.model.Place
import com.example.greenspots.network.RetrofitInstance
import com.google.android.gms.maps.model.LatLng
import dagger.Reusable
import javax.inject.Inject

@Reusable
class PlacesRepository @Inject constructor(
) {
    private val apiKey: String = BuildConfig.MAPS_API_KEY

    /**
     * Fetches a list of nearby places using the Google Places API and maps them to Place objects.
     *
     * @param location The LatLng object representing the user's current location.
     * @return A list of Place objects with details like id, name, location, and description.
     */
    suspend fun getNearbyPlaces(location: LatLng): List<Place> {
        // Perform the API call to get nearby places
        val response = RetrofitInstance.api.getNearbyPlaces(
            location = "${location.latitude},${location.longitude}",  // Pass the location as a string
            radius = 5000,  // 5 km radius for nearby search
            apiKey = apiKey  // Use the provided API key
        )

        // Map the results from the API to the Place data model
        return response.results.map { placeResult ->
            Place(
                id = placeResult.placeId,  // Use placeId for identification
                name = placeResult.name,
                location = LatLng(placeResult.geometry.location.lat, placeResult.geometry.location.lng),
                description = placeResult.vicinity  // Optional description (vicinity)
            )
        }
    }

    suspend fun getPlaceDetails(placeId: String): PlaceDetailsResult? {
        return try {
            val response = RetrofitInstance.api.getPlaceDetails(
                placeId = placeId,
                apiKey = BuildConfig.MAPS_API_KEY
            )
            if (response.result != null) {
                Log.d("PlacesRepository", "Place details fetched: ${response.result}")
                response.result
            } else {
                Log.e("PlacesRepository", "Error in response: ${response.status}")
                null
            }
        } catch (e: Exception) {
            Log.e("PlacesRepository", "Error fetching place details", e)
            null
        }
    }


    /**
     * Searches for places based on a query string using the Google Places API.
     *
     * @param query The search query (name or location).
     * @return A list of Place objects.
     */
    suspend fun searchPlaces(query: String): List<Place> {
        val response = RetrofitInstance.api.searchPlaces(
            query = query,
            apiKey = apiKey
        )

        // Map the API response to Place objects
        return response.results.map { placeResult ->
            Place(
                id = placeResult.placeId,  // Use placeId for identification
                name = placeResult.name,
                location = LatLng(
                    placeResult.geometry.location.lat,
                    placeResult.geometry.location.lng
                ),
                description = placeResult.vicinity  // Optional description (vicinity)
            )
        }
    }
}
