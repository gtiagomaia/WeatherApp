package com.tiagomaia.weatherapp.network

object OpenWeatherMapApi {
    // api key exposed only for test
    const val baseUrl = "https://api.openweathermap.org/data/2.5/"
    const val WeatherPathQuery = "weather?id=524901&appid=<APP_ID>=metric"
}
