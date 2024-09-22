package com.example.greenspots.favorites

import android.content.Context
import android.content.SharedPreferences
import com.example.greenspots.map.model.Place
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class FavoritesRepository constructor(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("favorites_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getFavoritePlaces(): List<Place> {
        val favoritesJson = sharedPreferences.getString("favorites", null)
        return if (favoritesJson != null) {
            val type = object : TypeToken<List<Place>>() {}.type
            gson.fromJson(favoritesJson, type)
        } else {
            emptyList()
        }
    }

    fun addFavoritePlace(place: Place) {
        val favorites = getFavoritePlaces().toMutableList()
        favorites.add(place)
        saveFavorites(favorites)
    }

    fun removeFavoritePlace(placeId: String) {
        val favorites = getFavoritePlaces().toMutableList()
        val updatedFavorites = favorites.filter { it.id != placeId }
        saveFavorites(updatedFavorites)
    }

    private fun saveFavorites(favorites: List<Place>) {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(favorites)
        editor.putString("favorites", json)
        editor.apply()
    }
}
