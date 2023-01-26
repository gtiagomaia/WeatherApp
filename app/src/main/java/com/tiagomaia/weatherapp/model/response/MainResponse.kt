package com.tiagomaia.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class MainResponse(
    val temp:Double? = null, // Temperature
    val pressure:Int? = null, // Atmospheric pressure, hPa
    val humidity:Int? = null, // in % [0..100]
    @SerializedName("feels_like") val feelsLike:Double? = null, //This temperature parameter accounts for the human perception of weather.
    @SerializedName("temp_min") val tempMin:Double? = null, //Atmospheric pressure on the sea level, hPa
    @SerializedName("temp_max") val tempMax:Double? = null, //Atmospheric pressure on the ground level, hPa
    @SerializedName("sea_level") val seaLevel:Int? = null, //Atmospheric pressure on the sea level, hPa
    @SerializedName("grnd_level") val groundLevel:Int? = null //Atmospheric pressure on the ground level, hPa
)