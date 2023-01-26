package com.tiagomaia.weatherapp.network


sealed class NetworkResult<T> {
    data class Loading<T>(val isLoading: Boolean) : NetworkResult<T>()
    data class Success<T>(val data: T) : NetworkResult<T>()
    data class Error<T>(val errorMessage: String) : NetworkResult<T>()
}