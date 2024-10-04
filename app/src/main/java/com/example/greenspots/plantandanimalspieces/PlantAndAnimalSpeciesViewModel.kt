package com.example.greenspots.plantandanimalspieces


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SpeciesViewModel @Inject constructor(private val speciesRepository: SpeciesRepository) :
    ViewModel() {

    private val _speciesList = MutableStateFlow<List<Species>>(emptyList())
    val speciesList: StateFlow<List<Species>> = _speciesList

    // Load species based on the Place ID
    fun loadSpeciesForPlace(placeId: String) {
        viewModelScope.launch {
            val species = speciesRepository.fetchSpeciesByPlaceId(placeId)
            _speciesList.value = species
        }
    }
}






