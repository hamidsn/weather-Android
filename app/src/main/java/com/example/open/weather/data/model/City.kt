package com.example.open.weather.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city_names")
data class City(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var name: String?,
    var zipCode: Int? = null
)
