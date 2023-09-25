package com.example.weather.weather.di

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.open.weather.BuildConfig
import com.example.open.weather.data.source.remote.OpenWeatherApi
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.mockk.mockk

import org.junit.Before
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RemoteModuleTest {
    private val fusedLocationProviderClient: FusedLocationProviderClient = mockk(relaxed = true)
    lateinit var context: Context

    @Before
    fun setup() {
        context = ApplicationProvider.getApplicationContext()
    }

    @Provides
    @Singleton
    fun provideOpenWeatherApi(): OpenWeatherApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }

    fun providesFusedLocationProviderClient(): FusedLocationProviderClient {
        return fusedLocationProviderClient
    }

}