package com.tiagomaia.weatherapp.model.usecase

data class Wind(
    val speed:Double,// Wind speed in Km/h
    val deg: Int, // Wind direction, degrees (meteorological)
    val gust: Double //Wind gust. Unit Default: meter/sec,
)
