package com.example.greenspots.map.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

/**
 * Entity Object: Represents a green spot or place of interest in the application.
 *
 * @property id Unique identifier for the place.
 * @property name Name of the place.
 * @property location The geographical location of the place, represented by a [LatLng] object.
 * @property description A brief description of the place (optional).
 */
@Parcelize
data class Place(
    val id: String,
    val name: String,
    val location: LatLng,
    val description: String? = null
) : Parcelable
