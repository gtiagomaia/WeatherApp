package com.tiagomaia.weatherapp.model.usecase

data class CurrentWeather(
    val coord: Coordinate,
    val weather:MainWeather,
    val base:String,
    val main: Main,
    val visibility: Int, // in KM
    val wind: Wind,
    val rain: Rain,
    val snow: Snow,
    val dt:Int, // unix timestamp
    val sys: SysInternalParams,
    val timezone: Int,
    val id:Int,
    val name:String
) {
    data class Main(
        val temp:Int, // Temperature
        val pressure:Int, // Atmospheric pressure, hPa
        val humidity:Int, // in % [0..100]
        val feelsLike:Double, //This temperature parameter accounts for the human perception of weather.
        val tempMin:Double, //Atmospheric pressure on the sea level, hPa
        val tempMax:Double, //Atmospheric pressure on the ground level, hPa
        val seaLevel:Int, //Atmospheric pressure on the sea level, hPa
        val groundLevel:Int //Atmospheric pressure on the ground level, hPa
    )
}

