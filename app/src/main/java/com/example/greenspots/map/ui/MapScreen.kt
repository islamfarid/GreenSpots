package com.example.greenspots.map.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

@Composable
fun MapScreen(
    navController: NavController,
    cameraPositionState: CameraPositionState,
    currentLocation: LatLng,  // Current location passed here
    viewModel: MapViewModel = hiltViewModel()  // Get the ViewModel using Hilt
) {
    // Trigger fetching of nearby places when the location changes
    LaunchedEffect(key1 = currentLocation) {
        viewModel.fetchNearbyPlaces(currentLocation)
    }

    // Collect the places state from ViewModel
    val places by viewModel.places.collectAsState()

    // State to manage the visibility of the dropdown menu
    var expanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /* Title of the screen or app */ },
                actions = {
                    // Add the search button to the top app bar
                    IconButton(onClick = { navController.navigate("search") }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search for places"
                        )
                    }

                    // Add the favorites button
                    IconButton(onClick = { navController.navigate("favorites") }) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Go to favorites"
                        )
                    }


                    // Overflow Menu (3 dots)
                    IconButton(onClick = { expanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }

                    // Dropdown Menu
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(onClick = {
                            navController.navigate("recommendations")
                            expanded = false
                        }) {
                            Text("Recommendations")
                        }
                        DropdownMenuItem(onClick = {
                            navController.navigate("categories")
                            expanded = false
                        }) {
                            Text("Explore Categories")
                        }
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
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(zoomControlsEnabled = true)
        ) {
            // Display markers for each place
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
