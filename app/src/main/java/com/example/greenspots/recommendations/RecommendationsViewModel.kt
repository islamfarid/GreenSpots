package com.example.greenspots.recommendations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.CommonRepository.PlacesRepository
import com.example.greenspots.map.model.Place
import com.example.greenspots.network.PlaceResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecommendationsViewModel @Inject constructor(private val repository: PlacesRepository) : ViewModel() {

        private val _recommendations = MutableStateFlow<List<PlaceResult>>(emptyList())
        val recommendations: StateFlow<List<PlaceResult>> = _recommendations

        fun fetchRecommendations(lat: Double, lng: Double, radius: Int = 1000, type: String = "park") {
            viewModelScope.launch {
                try {
                    val places = repository.getNearbyPlaces(lat, lng, radius, type)
                    _recommendations.value = places
                } catch (e: Exception) {
                    // Handle error
                }
            }
        }
    }
