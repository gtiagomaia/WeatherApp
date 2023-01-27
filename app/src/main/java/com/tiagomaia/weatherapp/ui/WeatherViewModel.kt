package com.tiagomaia.weatherapp.ui

import android.net.Network
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiagomaia.weatherapp.R
import com.tiagomaia.weatherapp.model.usecase.City
import com.tiagomaia.weatherapp.model.usecase.Coordinate
import com.tiagomaia.weatherapp.model.usecase.CurrentWeather
import com.tiagomaia.weatherapp.network.NetworkResult
import com.tiagomaia.weatherapp.network.OWMRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: OWMRepo
) : ViewModel() {
    private var _weather = MutableLiveData<CurrentWeather>()
    val weather: LiveData<CurrentWeather> = _weather

    private val _itemsState = mutableStateListOf<NetworkResult<CurrentWeather>>()
    val itemsState: List<NetworkResult<CurrentWeather>> = _itemsState



    fun getCurrentWeatherForLocation(lat:Double, lon:Double){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeatherFor(Coordinate(lat, lon)).collect {


                Log.d("weather",it.toString())

                when(it){
                    is NetworkResult.Loading -> {
                        Log.d("weather_request_loading", "$.isLoading: {it.isLoading}")
                    }

                    is NetworkResult.Error -> {
                       Log.d("weather_request_error", it.errorMessage)
                    }

                    is  NetworkResult.Success -> {
                        //success
                        _weather.postValue(it.data)
                    }
                }
            }
        }
    }
    fun getCurrentWeatherForCity(coord:Coordinate) = getCurrentWeatherForCity(coord.lat, coord.lon)
    fun getCurrentWeatherForCity(lat:Double, lon:Double){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeatherFor(Coordinate(lat, lon)).collect {
                Log.d("Items", it.toString())
                _itemsState.add(it)
                Log.d("Items", "itemState.size=${itemsState.size}")
            }
        }
    }


    fun requestForCities(cities:List<City>){

        cities.forEach {
            getCurrentWeatherForCity(it.coord)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }
}

