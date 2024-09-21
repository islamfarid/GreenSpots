package com.example.greenspots.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.greenspots.Repository.PlacesRepository
import com.example.greenspots.map.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {

    private val _searchResults = MutableStateFlow<List<Place>>(emptyList())
    val searchResults: StateFlow<List<Place>> = _searchResults.asStateFlow()

    /**
     * Performs a search for places based on the user input.
     *
     * @param query The search query from the user.
     */
    fun searchPlaces(query: String) {
        viewModelScope.launch {
            try {
                val results = repository.searchPlaces(query)
                _searchResults.value = results
            } catch (e: Exception) {
                // Handle error (log it or show a message)
                _searchResults.value = emptyList()
            }
        }
    }
}
