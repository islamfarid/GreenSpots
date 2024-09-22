package com.example.greenspots.commonusecases

import com.example.greenspots.favorites.FavoritesRepository
import com.example.greenspots.map.model.Place
import javax.inject.Inject

class AddPlaceToFavoritesUseCase @Inject constructor(private val repository: FavoritesRepository) {
    fun execute(place: Place) {
        repository.addFavoritePlace(place)
    }
}
