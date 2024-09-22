package com.example.greenspots.categories

import com.example.greenspots.BuildConfig
import com.example.greenspots.map.model.Place
import com.example.greenspots.network.RetrofitInstance
import com.google.android.gms.maps.model.LatLng
import dagger.Reusable
import javax.inject.Inject


@Reusable
class SpotsRepository @Inject constructor() {
    suspend fun getSpotsByCategoryAndLocation(
        categoryId: String,
        currentLocation: LatLng
    ): List<Place> {
        // Fetch spots from the API around the current location based on category (type)
        val response = RetrofitInstance.api.getNearbyPlaces(
            location = "${currentLocation.latitude},${currentLocation.longitude}",
            radius = 5000,    // Set the radius around the current location
            type = categoryId, // Map categoryId to the type (e.g., "park", "lake", "museum")
            apiKey = BuildConfig.MAPS_API_KEY // Make sure you have your API key here
        )

        // Map the API response to the Place object
        return response.results.map { placeResult ->
            Place(
                id = placeResult.placeId,
                name = placeResult.name,
                location = LatLng(
                    placeResult.geometry.location.lat,
                    placeResult.geometry.location.lng
                ),
                description = placeResult.vicinity
            )
        }
    }
}
