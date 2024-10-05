package com.example.greenspots.viewupcomingevents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EventsViewModel @Inject constructor(private val eventsRepository: EventsRepository) : ViewModel() {

    private val _events = MutableStateFlow<List<Event>>(emptyList())
    val events: StateFlow<List<Event>> = _events

    fun loadEventsForLocation(currentLocation: LatLng) {
        viewModelScope.launch {
            val events = eventsRepository.fetchEventsByLocation(currentLocation.latitude, currentLocation.longitude)
            _events.value = events
        }
    }
}
