package com.example.greenspots.categories

import com.example.greenspots.map.model.Place
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetSpotsByCategoryAndLocationUseCase @Inject constructor(
    private val repository: SpotsRepository
) {
    suspend fun execute(categoryId: String, currentLocation: LatLng): List<Place> {
        return repository.getSpotsByCategoryAndLocation(categoryId, currentLocation)
    }
}
