package com.example.greenspots.viewupcomingevents.details

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.greenspots.viewupcomingevents.Event
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

@Composable
fun EventDetailsScreen(
    navController: NavController,
    eventJson: String
) {
    // Deserialize event from JSON
    val decodedEventJson = URLDecoder.decode(eventJson, StandardCharsets.UTF_8.toString())
    val event: Event = Gson().fromJson(decodedEventJson, Event::class.java)

    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Event Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = event.name ?: "No Name Available", style = MaterialTheme.typography.h5, modifier = Modifier.padding(bottom = 8.dp))
            Text(text = "Date: ${event.dates?.start?.localDate ?: "No Date Available"}", modifier = Modifier.padding(bottom = 8.dp))
            val venue = event._embedded?.venues?.firstOrNull()
            Text(text = "Location: ${venue?.name ?: "Not available"}, ${venue?.address?.city ?: ""}", modifier = Modifier.padding(bottom = 8.dp))
            Text(text = event.description ?: "No Description Available")
            event.images?.firstOrNull()?.let { image ->
                Image(
                    painter = rememberImagePainter(data = image.url),
                    contentDescription = "Event Image",
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
            event.url?.let { url ->
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {
                            append("Link")
                        }
                    },
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            context.startActivity(intent)
                        },
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}