package com.example.weather.data

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.open.weather.data.AppDatabase
import com.example.open.weather.data.model.City
import com.example.open.weather.data.model.CityDao
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CityDataBaseTest {
    private lateinit var dataDao: CityDao
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Using an in-memory database because the information stored here disappears when the
        // process is killed.
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        dataDao = db.cityDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun insertCity() = runBlocking {
        val city = City(name = "London", zipCode = 1234, lat = 1.1, lon = 2.2, id = 123456789)
        dataDao.insertCity(city)
        assertEquals(dataDao.getCityByName("London")?.name, city.name)
    }

    @Test
    fun check_not_inserted_city() = runBlocking {
        val city1 = City(name = "London", zipCode = 1234, lat = 1.1, lon = 2.2, id = 123456789)
        val city2 = City(name = "Perth", zipCode = 1345, lat = 1.2, lon = 2.3, id = 123456789)
        dataDao.insertCity(city1)
        dataDao.insertCity(city2)
        assertEquals(dataDao.getCityByName("Moscow")?.name, null)
    }
}