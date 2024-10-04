package com.example.greenspots.plantandanimalspieces

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter


@OptIn(ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ViewPlantAndAnimalSpeciesScreen(
    navController: NavController,
    placeId: String,
    viewModel: SpeciesViewModel = hiltViewModel()
) {
    // Trigger loading of species data by place ID
    LaunchedEffect(placeId) {
        viewModel.loadSpeciesForPlace(placeId)
    }

    // Observe the species list
    val speciesList by viewModel.speciesList.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Plant & Animal Species") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        LazyColumn {
            itemsIndexed(speciesList) { index, species ->
                ListItem(
                    text = { Text(species.name) },
                    secondaryText = { Text(species.type) },
                    icon = {
                        Image(
                            painter = rememberImagePainter(data = species.imageUrl),
                            contentDescription = null,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                )
            }
        }

    }
}
