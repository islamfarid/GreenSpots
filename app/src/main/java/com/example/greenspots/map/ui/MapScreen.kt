package com.example.greenspots.map.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    navController: NavController,
    cameraPositionState: CameraPositionState,
    currentLocation: LatLng,  // Current location passed here
    viewModel: MapViewModel = hiltViewModel()
) {
    // Trigger fetching of nearby places when the location changes
    LaunchedEffect(key1 = currentLocation) {
        viewModel.fetchNearbyPlaces(currentLocation)
    }

    // Collect the places state as a `State` and access it using `by` delegation
    val places by viewModel.places.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Title of the screen or app */ },
                actions = {
                    // Add the search button to the top app bar
                    IconButton(onClick = {
                        navController.navigate("search")
                    }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search for places"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            properties = com.google.maps.android.compose.MapProperties(isMyLocationEnabled = true),
            uiSettings = com.google.maps.android.compose.MapUiSettings(zoomControlsEnabled = true)
        ) {
            places.forEach { place ->
                Marker(
                    state = MarkerState(position = place.location),
                    title = place.name,
                    snippet = place.description.orEmpty(),
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN),
                    onClick = {
                        navController.navigate("details/${place.id}") // Pass only the placeId
                        true
                    }
                )
            }
        }
    }
}
