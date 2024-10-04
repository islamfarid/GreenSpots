package com.example.greenspots.viewupcomingevents

import retrofit2.http.GET
import retrofit2.http.Query

interface EventsApiService {
    @GET("events.json")
    suspend fun getEventsByLocation(
        @Query("latlong") latLong: String,
        @Query("radius") radius: String = "10",
        @Query("apikey") apiKey: String,
        @Query("size") size: Int = 50  // Number of results
    ): EventsResponse
}


data class Event(
    val id: String,
    val name: String,
    val description: String?,
    val dates: Dates,
    val venue: Venue?
)

data class Dates(
    val start: Start
)

data class Start(
    val localDate: String
)

data class Venue(
    val name: String?
)

data class EventsResponse(
    val _embedded: EmbeddedEvents?
)

data class EmbeddedEvents(
    val events: List<Event>
)





