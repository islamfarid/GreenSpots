package com.example.greenspots.di

import android.content.Context
import com.example.greenspots.BuildConfig
import com.example.greenspots.favorites.FavoritesRepository
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private val apiKey: String = BuildConfig.MAPS_API_KEY

    @Provides
    @Singleton
    fun provideFavoritesRepository(@ApplicationContext context: Context): FavoritesRepository {
        return FavoritesRepository(context)
    }

    @Provides
    @Singleton
    fun providePlacesClient(@ApplicationContext context: Context): PlacesClient {
        Places.initialize(context, apiKey)
        return Places.createClient(context)
    }
}
