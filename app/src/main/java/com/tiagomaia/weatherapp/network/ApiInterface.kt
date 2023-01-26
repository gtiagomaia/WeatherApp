package com.tiagomaia.weatherapp.network


import com.tiagomaia.weatherapp.model.response.CurrentWeatherResponse
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiInterface {

    @POST(OpenWeatherMapApi.WeatherPathQuery)
    suspend fun getCurrentWeatherFor(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double
    ) : CurrentWeatherResponse

}