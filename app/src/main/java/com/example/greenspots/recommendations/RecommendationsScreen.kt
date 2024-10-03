package com.example.greenspots.recommendations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.greenspots.network.PlaceResult

@Composable
fun RecommendationsScreen(
    lat: Double,
    lng: Double,
    navController: NavController,
    viewModel: RecommendationsViewModel = hiltViewModel()
) {
    // Fetch recommendations when the screen loads
    LaunchedEffect(lat, lng) {
        viewModel.fetchRecommendations(lat, lng)
    }

    // Observe the list of recommendations
    val recommendations by viewModel.recommendations.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recommended Places") },
                navigationIcon = {
                    // Back navigation if necessary
                }
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()) {
            if (recommendations.isEmpty()) {
                Text("No recommendations available.", modifier = Modifier.padding(16.dp))
            } else {
                LazyColumn {
                    items(recommendations) { place ->
                        RecommendationItem(place = place, onClick = {
                            navController.navigate("details/${place.placeId}")
                        })
                    }
                }
            }
        }
    }
}

@Composable
fun RecommendationItem(place: PlaceResult, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(text = place.name, style = androidx.compose.material.MaterialTheme.typography.h6)
        Text(text = place.vicinity ?: "No address available", modifier = Modifier.padding(top = 8.dp))
    }
}
