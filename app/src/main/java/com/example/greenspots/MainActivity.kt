package com.example.greenspots

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.greenspots.categories.CategoriesScreen
import com.example.greenspots.categories.CategorySpotsScreen
import com.example.greenspots.details.DetailScreen
import com.example.greenspots.favorites.FavoriteScreen
import com.example.greenspots.map.ui.MapScreen
import com.example.greenspots.plantandanimalspieces.ViewPlantAndAnimalSpeciesScreen
import com.example.greenspots.recommendations.RecommendationsScreen
import com.example.greenspots.search.SearchScreen
import com.example.greenspots.ui.theme.GreenSpotsTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            GreenSpotsTheme {
                Surface(color = Color.White) {
                    val navController = rememberNavController()

                    var location by remember { mutableStateOf<LatLng?>(null) }
                    val cameraPositionState =
                        remember { mutableStateOf<CameraPositionState?>(null) }

                    // Check & Request Location Permission
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Fetch last known location if permission is granted
                        LaunchedEffect(Unit) {
                            getLastKnownLocation { loc ->
                                location = loc
                            }
                        }
                    } else {
                        // Request location permission if not granted
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                            REQUEST_LOCATION_PERMISSION
                        )
                    }

                    // Initialize cameraPositionState within a composable function
                    InitializeCameraState(location, cameraPositionState)

                    NavHost(navController = navController, startDestination = "map") {
                        composable("map") {
                            // Render MapScreen only when location and camera state are available
                            if (location != null && cameraPositionState.value != null) {
                                MapScreen(
                                    navController = navController,
                                    cameraPositionState = cameraPositionState.value!!,
                                    currentLocation = location!!
                                )
                            } else {
                                // Show loading message while location is being fetched
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "Loading location...")
                                }
                            }
                        }
                        // ... other composable routes (details, search) ...
                        composable(
                            route = "details/{placeId}",
                            arguments = listOf(navArgument("placeId") {
                                type = NavType.StringType
                            })
                        ) { backStackEntry ->
                            val placeId = backStackEntry.arguments?.getString("placeId")
                            DetailScreen(placeId = placeId, navController = navController)
                        }
                        composable("search") {
                            SearchScreen(navController = navController)
                        }

                        composable("favorites") {
                            FavoriteScreen(navController = navController)
                        }

                        composable("categories") {
                            CategoriesScreen(navController = navController)
                        }
                        composable("category/{categoryId}") { backStackEntry ->
                            val categoryId = backStackEntry.arguments?.getString("categoryId")

                            CategorySpotsScreen(
                                categoryId = categoryId ?: "Park",
                                currentLocation = location!!,  // Pass the current location here
                                navController = navController
                            )
                        }
                        // Add the RecommendationsScreen route
                        composable("recommendations") {
                            RecommendationsScreen(
                                lat = location?.latitude ?: 0.0,
                                lng = location?.longitude ?: 0.0,
                                navController = navController
                            )
                        }

                        composable("viewPlantAndAnimalSpecies/{placeId}") { backStackEntry ->
                            val placeId = backStackEntry.arguments?.getString("placeId")
                            placeId?.let {
                                ViewPlantAndAnimalSpeciesScreen(
                                    navController = navController,
                                    placeId = it
                                )
                            }
                        }


                    }
                }
            }
        }
    }

    // Get the last known location using FusedLocationProviderClient
    private fun getLastKnownLocation(callback: (LatLng) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.let {
                    callback(LatLng(it.latitude, it.longitude))
                }
            }
    }

    // Composable function to initialize cameraPositionState
    @Composable
    private fun InitializeCameraState(
        location: LatLng?,
        cameraPositionState: MutableState<CameraPositionState?>
    ) {
        location?.let { loc ->
            cameraPositionState.value = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(loc, 12f)
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}