package com.example.weather.data

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.open.weather.data.AppDatabase
import com.example.open.weather.data.model.CityDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.junit.After
import org.junit.Assert.*
import java.io.IOException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FakeCityDataBase {
    private lateinit var dataDao: CityDao
    private lateinit var db: AppDatabase


    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Provides
    @Singleton
    fun provideDataBaseDao(): CityDao {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        dataDao = db.cityDao()
        return db.cityDao()
    }

}