package com.example.greenspots.map.data

import com.example.greenspots.BuildConfig
import com.example.greenspots.map.model.Place
import com.example.greenspots.network.RetrofitInstance
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PlacesRepository @Inject constructor(
) {
    private val apiKey: String = BuildConfig.MAPS_API_KEY

    suspend fun getNearbyPlaces(location: LatLng): List<Place> {
        // Access the API via RetrofitInstance
        val response = RetrofitInstance.api.getNearbyPlaces(
            location = "${location.latitude},${location.longitude}",
            radius = 5000, // 5 km radius
            apiKey = apiKey
        )

        return response.results.map { placeResult ->
            Place(
                id = placeResult.name,
                name = placeResult.name,
                location = LatLng(placeResult.geometry.location.lat, placeResult.geometry.location.lng),
                description = placeResult.vicinity
            )
        }
    }
}
