package com.example.greenspots.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.R
import com.example.greenspots.map.model.Place
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val getSpotsByCategoryAndLocationUseCase: GetSpotsByCategoryAndLocationUseCase
) : ViewModel() {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories

    private val _spots = MutableStateFlow<List<Place>>(emptyList())
    val spots: StateFlow<List<Place>> = _spots

    init {
        loadCategories()
    }

    private fun loadCategories() {
        _categories.value = listOf(
            Category(id = "park", name = "Parks", icon = R.drawable.ic_park),
            Category(id = "lake", name = "Lakes", icon = R.drawable.ic_lake),
            Category(id = "forest", name = "Forests", icon = R.drawable.ic_forest),
            Category(
                id = "nature_reserve",
                name = "Nature Reserves",
                icon = R.drawable.ic_nature_reserve
            )
        )
    }

    // Fetch spots by category around the current location
    fun getSpotsForCategory(categoryId: String, currentLocation: LatLng) {
        viewModelScope.launch {
            val places = getSpotsByCategoryAndLocationUseCase.execute(categoryId, currentLocation)
            _spots.value = places
        }
    }
}
