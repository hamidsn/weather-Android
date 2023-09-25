package com.example.open.weather.data.model.weather

import com.google.gson.annotations.SerializedName


data class Coord(

    @SerializedName("lon") var lon: Double? = null,
    @SerializedName("lat") var lat: Double? = null

)