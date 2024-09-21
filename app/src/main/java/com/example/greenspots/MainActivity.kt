package com.example.greenspots

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.ui.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.greenspots.map.ui.MapScreen
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
    private lateinit var cameraPositionState: CameraPositionState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        } else {
            getLastKnownLocation { location ->
                setContent {
                    GreenSpotsTheme {
                        Surface(color = Color.White) {
                            val navController = rememberNavController()
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(location, 12f)
                            }

                            NavHost(navController = navController, startDestination = "map") {
                                composable("map") {
                                    MapScreen(
                                        navController = navController,
                                        cameraPositionState = cameraPositionState,
                                        currentLocation = location // Pass current location
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getLastKnownLocation(callback: (LatLng) -> Unit) {
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                callback(LatLng(it.latitude, it.longitude))
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastKnownLocation { location ->
                    // Update the content with the location
                    setContent {
                        GreenSpotsTheme {
                            Surface(color = Color.White) {
                                val navController = rememberNavController()
                                cameraPositionState = rememberCameraPositionState {
                                    position = CameraPosition.fromLatLngZoom(location, 15f)
                                }

                                NavHost(navController = navController, startDestination = "map") {
                                    composable("map") {
                                        MapScreen(
                                            navController = navController,
                                            cameraPositionState = cameraPositionState,
                                            currentLocation = location // Pass current location
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Permission denied - handle accordingly
            }
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }
}
