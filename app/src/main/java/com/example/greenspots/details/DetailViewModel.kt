package com.example.greenspots.details

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.CommonRepository.PlacesRepository
import com.example.greenspots.commonusecases.AddPlaceToFavoritesUseCase
import com.example.greenspots.commonusecases.RemovePlaceFromFavoritesUseCase
import com.example.greenspots.details.model.PlaceDetailsResult
import com.example.greenspots.favorites.GetFavoritePlacesUseCase
import com.example.greenspots.map.model.Place
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
    private val repository: PlacesRepository,
    private val addPlaceToFavoritesUseCase: AddPlaceToFavoritesUseCase,
    private val removePlaceFromFavoritesUseCase: RemovePlaceFromFavoritesUseCase,
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase
) : ViewModel() {

    private val _placeDetails = MutableStateFlow<PlaceDetailsResult?>(null)
    val placeDetails: StateFlow<PlaceDetailsResult?> = _placeDetails.asStateFlow()

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    fun fetchPlaceDetails(placeId: String) {
        Log.d("DetailViewModel", "Fetching details for placeId: $placeId")
        viewModelScope.launch {
            _placeDetails.value = null
            val details = withContext(Dispatchers.IO) {
                repository.getPlaceDetails(placeId)
            }
            _placeDetails.value = details

            // Check if the place is already a favorite
            details?.let { place ->
                _isFavorite.value = getFavoritePlacesUseCase.execute().any { it.id == place.id }
            }
        }
    }

    fun toggleFavorite(place: Place) {
        viewModelScope.launch {
            if (_isFavorite.value) {
                removePlaceFromFavoritesUseCase.execute(place.id)
            } else {
                addPlaceToFavoritesUseCase.execute(place)
            }
            _isFavorite.value = !_isFavorite.value
        }
    }
}


