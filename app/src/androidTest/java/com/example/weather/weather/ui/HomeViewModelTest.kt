package com.example.weather.weather.ui

import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import com.example.open.weather.data.AppDatabase
import com.example.open.weather.data.model.City
import com.example.open.weather.data.model.CityDao
import com.example.open.weather.data.model.weather.CurrentWeatherModel
import com.example.open.weather.data.repositories.LocalDatabaseRepositoryImpl
import com.example.open.weather.domain.use_case.GetCurrentWeatherUseCase
import com.example.open.weather.ui.viewmodel.HomeViewModel
import com.example.weather.data.FakeCityDataBase.provideDataBaseDao
import com.example.weather.weather.data.repositories.FakeWeatherRepositoryImpl
import com.example.weather.weather.di.RemoteModuleTest.provideOpenWeatherApi
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class HomeViewModelTest {

    private lateinit var dataDao: CityDao
    private lateinit var db: AppDatabase

    // Use a fake repository to be injected into the view<M>odel
    private lateinit var tasksRepository: FakeWeatherRepositoryImpl
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var weatherData: CurrentWeatherModel

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setupViewModel() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .build()
        dataDao = db.cityDao()
        tasksRepository = FakeWeatherRepositoryImpl(
            api = provideOpenWeatherApi(),
            cityDao = provideDataBaseDao(),
        )

        var gson = Gson()
        weatherData = gson.fromJson(
            """{"coord":{"lon":-0.1257,"lat":51.5085},"weather":[{"id":802,"main":"Clouds","description":"scattered clouds","icon":"03d"}],"base":"stations","main":{"temp":285.68,"feels_like":285.21,"temp_min":284.02,"temp_max":286.63,"pressure":1014,"humidity":85},"visibility":10000,"wind":{"speed":4.12,"deg":230},"clouds":{"all":40},"dt":1691473759,"sys":{"type":2,"id":2075535,"country":"GB","sunrise":1691469261,"sunset":1691523486},"timezone":3600,"id":2643743,"name":"London","cod":200}""".trimMargin(),
            CurrentWeatherModel::class.java
        )

        homeViewModel = HomeViewModel(
            null,
            GetCurrentWeatherUseCase(tasksRepository),
            LocalDatabaseRepositoryImpl(provideDataBaseDao())
        )
        val city1 = City(name = "London", zipCode = 1234, lat = 1.1, lon = 2.2, id = 123456789)
        val city2 = City(name = "Perth", zipCode = 1345, lat = 1.2, lon = 2.3, id = 123456789)
        runBlocking {
            dataDao.insertCity(city1)
            dataDao.insertCity(city2)
        }

    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun deleteCityTest() {
        homeViewModel.deleteCity("London")
        runBlocking {
            Assert.assertEquals(dataDao.getCityByName("Moscow")?.name, null)
            Assert.assertEquals(dataDao.getCityByName("London")?.name, null)
            Assert.assertEquals(dataDao.getCityByName("Perth")?.name, "Perth")
        }
    }

    @Test
    fun getWeatherOfACity() {
        val city1 = City(name = "London", zipCode = 1234, lat = 1.1, lon = 2.2, id = 123456789)
        runBlocking {
            homeViewModel.getCurrentWeather(city1)
            Assert.assertEquals("14:34:21", homeViewModel.state.value.sunRise)
        }
    }


}