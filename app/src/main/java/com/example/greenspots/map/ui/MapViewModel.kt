package com.example.greenspots.map.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.map.data.PlacesRepository
import com.example.greenspots.map.model.Place
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {

    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places

    /**
     * Fetches a list of nearby places from the [PlacesRepository] and updates the state.
     *
     * @param location The location around which to search for places.
     */
    fun fetchNearbyPlaces(location: LatLng) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val places = repository.getNearbyPlaces(location)
                // Update the UI state on the main thread
                withContext(Dispatchers.Main) {
                    _places.value = places
                }
            }
        }
    }
}
