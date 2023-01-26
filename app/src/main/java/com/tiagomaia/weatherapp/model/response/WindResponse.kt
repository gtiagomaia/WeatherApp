package com.tiagomaia.weatherapp.model.response

data class WindResponse(
    val speed:Double? = null,// Wind speed in Km/h
    val deg: Int? = null, // Wind direction, degrees (meteorological)
    val gust: Double? = null //Wind gust. Unit Default: meter/sec,
)