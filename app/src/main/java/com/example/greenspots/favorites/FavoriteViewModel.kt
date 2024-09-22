package com.example.greenspots.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.commonusecases.AddPlaceToFavoritesUseCase
import com.example.greenspots.commonusecases.RemovePlaceFromFavoritesUseCase
import com.example.greenspots.map.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val addPlaceToFavoritesUseCase: AddPlaceToFavoritesUseCase,
    private val removePlaceFromFavoritesUseCase: RemovePlaceFromFavoritesUseCase,
    private val getFavoritePlacesUseCase: GetFavoritePlacesUseCase
) : ViewModel() {

    private val _favoritePlaces = MutableStateFlow<List<Place>>(emptyList())
    val favoritePlaces: StateFlow<List<Place>> = _favoritePlaces

    init {
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            val favorites = getFavoritePlacesUseCase.execute()
            _favoritePlaces.value = favorites
        }
    }

    fun addFavoritePlace(place: Place) {
        viewModelScope.launch {
            addPlaceToFavoritesUseCase.execute(place)
            loadFavorites()  // Refresh the favorites after adding
        }
    }

    fun removeFavorite(placeId: String) {
        viewModelScope.launch {
            removePlaceFromFavoritesUseCase.execute(placeId)
            loadFavorites()  // Refresh the favorites after removing
        }
    }
}

