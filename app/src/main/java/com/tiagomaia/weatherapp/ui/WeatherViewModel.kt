package com.tiagomaia.weatherapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiagomaia.weatherapp.R
import com.tiagomaia.weatherapp.model.usecase.Coordinate
import com.tiagomaia.weatherapp.model.usecase.CurrentWeather
import com.tiagomaia.weatherapp.network.NetworkResult
import com.tiagomaia.weatherapp.network.OWMRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: OWMRepo
) : ViewModel() {
    private var _weather = MutableLiveData<CurrentWeather>()
    val weather: LiveData<CurrentWeather> = _weather

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

}

