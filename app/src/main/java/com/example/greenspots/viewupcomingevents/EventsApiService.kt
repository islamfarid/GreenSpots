package com.example.greenspots.viewupcomingevents

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Inject

interface EventsApiService {
    @GET("events")
    suspend fun getEventsByLocation(
        @Query("latlong") latlong: String,
        @Query("radius") radius: String = "10",
        @Query("apikey") token: String,
        @Query("size") perPage: Int = 50  // Number of results
    ): EventsResponse
}


data class Event(
    val id: String,
    val name: String?,
    val description: String?,
    val dates: EventDates?,
    val _embedded: EmbeddedLocation? = null,  // Updated to use EmbeddedLocation
    val images: List<EventImage>?, // Added images
    val url: String? // Added URL for event details
)

data class EventDates(
    val start: EventStart?
)

data class EventStart(
    val localDate: String?
)

data class EmbeddedLocation(  // New data class for embedded location details
    val venues: List<Venue>?
)

data class Venue(
    val name: String?,
    val address: Address?
)

data class Address(
    val line1: String?,
    val city: String?,
    val state: String?,
    val postalCode: String?
)

data class EventImage(  // New data class for event images
    val ratio: String?,
    val url: String?,
    val width: Int?,
    val height: Int?
)

data class EventsResponse(
    val _embedded: EmbeddedEvents?
)

data class EmbeddedEvents(
    val events: List<Event>?
)
