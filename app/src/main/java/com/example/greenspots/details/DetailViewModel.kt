package com.example.greenspots.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.Repository.PlacesRepository
import com.example.greenspots.details.model.PlaceDetailsResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {

    private val _placeDetails = MutableStateFlow<PlaceDetailsResult?>(null)
    val placeDetails: StateFlow<PlaceDetailsResult?> = _placeDetails.asStateFlow()

    fun fetchPlaceDetails(placeId: String) {
        Log.d("DetailViewModel", "Fetching details for placeId: $placeId")
        viewModelScope.launch {
            _placeDetails.value = null
            val details = withContext(Dispatchers.IO) {
                repository.getPlaceDetails(placeId)
            }
            Log.d("DetailViewModel", "Fetched details: $details")
            _placeDetails.value = details
        }
    }

}

