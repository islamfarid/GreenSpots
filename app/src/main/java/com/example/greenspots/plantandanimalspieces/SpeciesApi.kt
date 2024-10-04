package com.example.greenspots.plantandanimalspieces

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface SpeciesApiService {
    @GET
    suspend fun getSpeciesByLocation(
        @Url url: String,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("radius") radius: Int = 10,  // Radius in kilometers
        @Query("per_page") perPage: Int = 50  // Number of results per page
    ): SpeciesResponse
}

data class Species(
    val id: Int,
    val name: String,  // Species name
    val type: String,  // Could be plant, animal, etc. based on taxon rank
    val description: String,  // Additional info or description
    val location: LatLng,  // The location where the species was found
    val habitat: String,
    val imageUrl: String
)

// Response object for API
data class SpeciesResponse(
    @SerializedName("results")
    val results: List<Observation>
)

// Individual species observation
data class Observation(
    @SerializedName("id")
    val id: Int,

    @SerializedName("species_guess")
    val speciesGuess: String?,  // Species name (nullable in case it’s missing)

    @SerializedName("taxon")
    val taxon: Taxon?,

    @SerializedName("geojson")
    val geoJson: GeoJson?
)

// Taxon data for species
data class Taxon(
    @SerializedName("name")
    val name: String,

    @SerializedName("rank")
    val rank: String,

    @SerializedName("default_photo")
    val defaultPhoto: Photo?  // Image data, nullable in case the photo doesn't exist
)

// Photo class to handle image URLs
data class Photo(
    @SerializedName("url")
    val url: String  // URL for species image
)

// GeoJson data for location
data class GeoJson(
    @SerializedName("coordinates")
    val coordinates: List<Double>  // Latitude and longitude
)
