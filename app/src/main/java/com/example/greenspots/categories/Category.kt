package com.example.greenspots.categories

data class Category(
    val id: String,
    val name: String,
    val icon: Int // This can be a drawable resource id, or a URL if using online resources
)
