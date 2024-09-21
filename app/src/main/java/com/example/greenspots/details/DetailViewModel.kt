package com.example.greenspots.details

import androidx.lifecycle.ViewModel
import com.example.greenspots.map.data.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: PlacesRepository
) : ViewModel() {

    // Future implementations can include additional data fetching or business logic
}
