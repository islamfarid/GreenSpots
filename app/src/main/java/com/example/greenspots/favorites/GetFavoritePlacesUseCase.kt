package com.example.greenspots.favorites

import com.example.greenspots.map.model.Place
import javax.inject.Inject

class GetFavoritePlacesUseCase @Inject constructor(private val repository: FavoritesRepository) {
    fun execute(): List<Place> {
        return repository.getFavoritePlaces()
    }
}
