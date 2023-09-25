package com.example.open.weather.data.model.weather

import com.google.gson.annotations.SerializedName


data class Rain(

    @SerializedName("1h") var rain1h: Double? = null

)