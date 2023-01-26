package com.tiagomaia.weatherapp.network

import com.tiagomaia.weatherapp.model.usecase.Coordinate
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class OWMRepo @Inject constructor(private val apiInterface: ApiInterface) {

    suspend fun getCurrentWeatherFor(coordinate: Coordinate)  = flow {
        emit(NetworkResult.Loading(true))
        val response = apiInterface.getCurrentWeatherFor(coordinate.lat, coordinate.lon)
        emit(NetworkResult.Success(response.toModel()))
    }.catch { e ->
        emit(NetworkResult.Error(e.message ?: "Unknown Error"))
    }

}