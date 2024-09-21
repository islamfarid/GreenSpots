package com.example.greenspots.network


import com.google.gson.annotations.SerializedName

data class PlacesResponse(
    @SerializedName("results") val results: List<PlaceResult>
)

data class PlaceResult(
    @SerializedName("place_id") val placeId: String,  // Use the correct placeId
    @SerializedName("name") val name: String,
    @SerializedName("geometry") val geometry: Geometry,
    @SerializedName("vicinity") val vicinity: String?
)


data class Geometry(
    @SerializedName("location") val location: Location
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)
