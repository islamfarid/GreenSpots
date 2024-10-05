package com.example.greenspots.viewupcomingevents

import android.util.Log
import com.example.greenspots.network.RetrofitInstance
import javax.inject.Inject



class EventsRepository @Inject constructor() {
    private val apiKey = "EG3LsI9OcSZZIXcjexnohrD57lKtAiuG"

    suspend fun fetchEventsByLocation(lat: Double, lng: Double): List<Event> {
        return try {
            val latlong = "$lat,$lng"
            val response = RetrofitInstance.eventsApi.getEventsByLocation(
                latlong = latlong,
                token = apiKey
            )
            response._embedded?.events ?: emptyList()
        } catch (e: Exception) {
            Log.e("EventsRepository", "Error fetching events: \${e.message}")
            emptyList()
        }
    }
}

