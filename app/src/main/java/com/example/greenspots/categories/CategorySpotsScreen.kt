package com.example.greenspots.categories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.greenspots.map.model.Place
import com.google.android.gms.maps.model.LatLng

@Composable
fun CategorySpotsScreen(
    categoryId: String,
    currentLocation: LatLng,  // Pass the current location to this screen
    navController: NavController,
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    // Trigger fetching of spots when the screen loads
    LaunchedEffect(categoryId, currentLocation) {
        viewModel.getSpotsForCategory(categoryId, currentLocation)
    }

    // Collect the spots as a State with an initial empty list
    val spots by viewModel.spots.collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spots in Category") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            if (spots.isEmpty()) {
                Text("No spots available in this category.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    itemsIndexed(spots as List<Place>) { index, spot ->
                        // Display the name of the Place object and make it clickable
                        Text(
                            text = spot.name,
                            modifier = Modifier
                                .padding(16.dp)
                                .clickable {
                                    // Navigate to the details screen when clicked
                                    navController.navigate("details/${spot.id}")
                                }
                        )
                    }
                }
            }
        }
    }
}
