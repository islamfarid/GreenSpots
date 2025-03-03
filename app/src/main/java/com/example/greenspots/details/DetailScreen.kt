package com.example.greenspots.details

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.greenspots.BuildConfig
import com.example.greenspots.map.model.Place
import com.google.android.gms.maps.model.LatLng

@Composable
fun DetailScreen(
    placeId: String?,
    navController: NavController,
    viewModel: DetailViewModel = hiltViewModel()
) {
    // Context to use for the sharing functionality and opening Google Maps/Browser
    val context = LocalContext.current

    // Fetch place details when the screen loads
    LaunchedEffect(placeId) {
        placeId?.let { viewModel.fetchPlaceDetails(it) }
    }

    val placeDetails by viewModel.placeDetails.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()

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
                        },
                        actions = {
                            IconButton(
                                onClick = {
                                    placeDetails?.let { details ->
                                        viewModel.toggleFavorite(
                                            Place(
                                                id = details.id,
                                                name = details.name,
                                                location = LatLng(
                                                    details.geometry?.location?.lat ?: 0.0,
                                                    details.geometry?.location?.lng ?: 0.0
                                                ),
                                                description = details.address
                                            )
                                        )
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                                )
                            }
                            // Add share button for sharing place details
                            IconButton(
                                onClick = {
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "text/plain"
                                        putExtra(Intent.EXTRA_SUBJECT, "Check out this place!")
                                        putExtra(Intent.EXTRA_TEXT, """
                                            Check out ${details.name} at ${details.address}.
                                            You can call them at ${details.phoneNumber ?: "N/A"}.
                                            Visit their website: ${details.website ?: "N/A"}
                                        """.trimIndent())
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share via"))
                                }
                            ) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Share place details")
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
                        .verticalScroll(scrollState)
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

                    details.photos?.let { photos ->
                        val photoUrls = photos.map { photo ->
                            "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=${photo.photoReference}&key=${BuildConfig.MAPS_API_KEY}"
                        }
                        DisplayPhotos(photoUrls)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Button to open Google Maps/Chrome review page
                    placeDetails?.let {
                        Button(onClick = {
                            val searchQuery = Uri.encode("${it.name} ${it.address}")
                            val mapUrl =
                                "https://www.google.com/maps/search/?api=1&query=$searchQuery"
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                            context.startActivity(intent)
                        }) {
                            Text("Write a Review on Google")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // New button to navigate to ViewPlantAndAnimalSpeciesScreen
                    Button(
                        onClick = {
                            // Navigate to the Plant and Animal Species screen, passing the placeId
                            navController.navigate("viewPlantAndAnimalSpecies/${placeId}")
                        }
                    ) {
                        Text(text = "View Plant & Animal Species")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            placeId?.let {
                                navController.navigate("viewUpcomingEvents/$it")
                            }
                        }
                    ) {
                        Text(text = "View Upcoming Events")
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
