package com.example.greenspots.details.model

import com.google.gson.annotations.SerializedName

data class PlaceDetailsResponse(
    @SerializedName("result") val result: PlaceDetailsResult?,
    @SerializedName("status") val status: String
)


data class PlaceDetailsResult(
    @SerializedName("place_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("formatted_address") val address: String?,
    @SerializedName("formatted_phone_number") val phoneNumber: String?,
    @SerializedName("website") val website: String?,
    @SerializedName("opening_hours") val openingHours: OpeningHours?,
    @SerializedName("rating") val rating: Float?,
    @SerializedName("reviews") val reviews: List<Review>?,
    @SerializedName("photos") val photos: List<Photo>?,
    @SerializedName("geometry") val geometry: Geometry?  // Add geometry for location
)

data class Geometry(
    @SerializedName("location") val location: Location
)

data class Location(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lng") val lng: Double
)


data class OpeningHours(
    @SerializedName("weekday_text") val weekdayText: List<String>
)

data class Review(
    @SerializedName("author_name") val authorName: String,
    @SerializedName("rating") val rating: Float,
    @SerializedName("text") val text: String
)

data class Photo(
    @SerializedName("photo_reference") val photoReference: String
)
