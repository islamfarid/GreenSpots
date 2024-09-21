package com.example.greenspots.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.greenspots.BuildConfig

@Composable
fun DetailScreen(
    placeId: String?,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    // Fetch place details when the screen loads
    LaunchedEffect(placeId) {
        placeId?.let { viewModel.fetchPlaceDetails(it) }
    }

    val placeDetails by viewModel.placeDetails.collectAsState()

    // If details are not available (still loading), show a loading indicator
    if (placeDetails == null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        placeDetails?.let { details ->
            // Make the screen scrollable by wrapping the content inside a Column with verticalScroll
            val scrollState = rememberScrollState()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = details.name) },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back"
                                )
                            }
                        }
                    )
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(16.dp)
                        .fillMaxSize()
                        .verticalScroll(scrollState) // Enable vertical scrolling
                ) {
                    Text(text = details.name, style = MaterialTheme.typography.h4)
                    Text(text = details.address ?: "Address not available")
                    Text(text = "Phone: ${details.phoneNumber ?: "N/A"}")
                    Text(text = "Website: ${details.website ?: "N/A"}")

                    Spacer(modifier = Modifier.height(8.dp))

                    details.openingHours?.let {
                        Text(text = "Opening Hours:")
                        it.weekdayText.forEach { day ->
                            Text(text = day)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    details.reviews?.let { reviews ->
                        Text(text = "Reviews:", style = MaterialTheme.typography.h6)
                        reviews.forEach { review ->
                            Text(text = "${review.authorName}: ${review.rating}")
                            Text(text = review.text)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Add a placeholder image if needed here
                    details.photos?.let { photos ->
                        val photoUrls = photos.map { photo ->
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${photo.photoReference}&key=${BuildConfig.MAPS_API_KEY}"
                        }
                        DisplayPhotos(photoUrls)
                    }

                }
            }
        }
    }
}

@Composable
fun DisplayPhotos(photoUrls: List<String>) {
    LazyRow {
        items(photoUrls) { url ->
            Image(
                painter = rememberImagePainter(url),
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp)
                    .padding(8.dp)
            )
        }
    }
}


