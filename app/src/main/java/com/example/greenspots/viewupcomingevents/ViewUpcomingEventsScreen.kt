package com.example.greenspots.viewupcomingevents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.ListItem
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
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.greenspots.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ViewUpcomingEventsScreen(
    navController: NavController,
    currentLocation: LatLng,
    viewModel: EventsViewModel = hiltViewModel()
) {
    // Fetch the events for the given location
    LaunchedEffect(currentLocation) {
        viewModel.loadEventsForLocation(currentLocation)
    }

    // Observe the events list
    val events by viewModel.events.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Upcoming Events") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding)
        ) {
            itemsIndexed(events) { index, event ->
                ListItem(
                    text = { Text(event.name ?: "No Name Available") },
                    secondaryText = { Text(event.dates?.start?.localDate ?: "No Date Available") },
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.event),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.clickable {

                        val eventJson = Gson().toJson(event)
                        val encodedEventJson =
                            URLEncoder.encode(eventJson, StandardCharsets.UTF_8.toString())
                        navController.navigate("eventDetails/$encodedEventJson")


                    }
                )
            }
        }
    }
}
