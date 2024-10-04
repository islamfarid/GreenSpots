package com.example.greenspots.plantandanimalspieces

import android.util.Log
import com.example.greenspots.network.RetrofitInstance
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


class SpeciesRepository @Inject constructor(private val placesClient: PlacesClient) {

    suspend fun fetchSpeciesByPlaceId(placeId: String): List<Species> {
        return try {
            // Assuming the external API accepts placeId to fetch species information
            val latLongOfPlace = getLatLngFromPlaceId(placeId)
            val response = RetrofitInstance.speciesApi.getSpeciesByLocation(
                url = "https://api.inaturalist.org/v1/observations",
                lat = latLongOfPlace?.latitude!!,
                lng = latLongOfPlace.longitude!!
            )

            // Map the API response to your Species data model
            response.results.map {
                Species(
                    id = it.id,
                    name = it.speciesGuess
                        ?: "Unknown Species",  // Fallback to "Unknown" if name is null
                    type = it.taxon?.rank ?: "Unknown",  // Handle missing rank with a fallback
                    habitat = it.taxon?.name
                        ?: "Unknown Habitat",  // Assuming taxon name represents the habitat
                    location = LatLng(
                        it.geoJson?.coordinates?.get(1) ?: 0.0,
                        it.geoJson?.coordinates?.get(0) ?: 0.0
                    ),  // Safeguard against null or missing coordinates
                    imageUrl = it.taxon?.defaultPhoto?.url
                        ?: ""  // Safeguard against missing photo,
                    ,
                    description = "Species: ${it.taxon?.name ?: "Unknown"} (Rank: ${it.taxon?.rank ?: "Unknown"})"
                )

            }
        } catch (e: Exception) {
            Log.e("SpeciesRepository", "Error fetching species: ${e.message}")
            emptyList()  // Return an empty list if the fetch fails
        }
    }

    // Fetch the latitude and longitude using the Place ID
    suspend fun getLatLngFromPlaceId(placeId: String): LatLng? {
        return try {
            val placeFields = listOf(Place.Field.LAT_LNG)
            val request = FetchPlaceRequest.newInstance(placeId, placeFields)
            val response = placesClient.fetchPlace(request).awaitResponse()
            response.place.latLng
        } catch (e: Exception) {
            Log.e("SpeciesRepository", "Error fetching place details: ${e.message}")
            null
        }
    }

    // Helper function to convert Google Task to a coroutine
    suspend fun <T> Task<T>.awaitResponse(): T = suspendCancellableCoroutine { continuation ->
        addOnSuccessListener { result ->
            continuation.resume(result)
        }
        addOnFailureListener { exception ->
            continuation.resumeWithException(exception)
        }

        continuation.invokeOnCancellation {
            cancel()  // This will cancel the task if the coroutine is canceled
        }
    }

}




