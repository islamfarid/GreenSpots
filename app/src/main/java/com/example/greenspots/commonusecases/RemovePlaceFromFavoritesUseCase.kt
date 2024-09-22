package com.example.greenspots.commonusecases

import com.example.greenspots.favorites.FavoritesRepository
import javax.inject.Inject

class RemovePlaceFromFavoritesUseCase @Inject constructor(private val repository: FavoritesRepository) {
    fun execute(placeId: String) {
        repository.removeFavoritePlace(placeId)
    }
}
