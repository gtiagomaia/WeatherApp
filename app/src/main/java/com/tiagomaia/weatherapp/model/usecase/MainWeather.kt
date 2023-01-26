package com.tiagomaia.weatherapp.model.usecase

data class MainWeather(
    val id:Int, // Weather condition id
    val main:String, //Group of weather parameters (Rain, Snow, Extreme etc.)
    val description:String, //Weather condition within the group.
    val icon:String // Weather icon id
)
