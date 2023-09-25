package com.example.open.weather.di

import android.content.Context
import androidx.room.Room
import com.example.open.weather.data.AppDatabase
import com.example.open.weather.data.model.CityDao
import com.example.open.weather.data.repositories.LocalDatabaseRepositoryImpl
import com.example.open.weather.domain.repositories.LocalDatabaseRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    fun provideCityDao(appDatabase: AppDatabase): CityDao {
        return appDatabase.cityDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun getLocalDatabaseRepository(
        db: AppDatabase
    ): LocalDatabaseRepository = LocalDatabaseRepositoryImpl(db.cityDao())
}