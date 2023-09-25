package com.example.open.weather.di

import com.example.open.weather.data.repositories.WeatherRepositoryImpl
import com.example.open.weather.domain.repositories.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

/**
 * This class should only be referenced by generated code.
 */
@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoriesModule {

    @Binds
    abstract fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository
}