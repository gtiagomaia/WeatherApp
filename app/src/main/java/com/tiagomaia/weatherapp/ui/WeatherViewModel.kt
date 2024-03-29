package com.tiagomaia.weatherapp.ui

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tiagomaia.weatherapp.location.LocationManager
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
    private val repository: OWMRepo,
    private val locationManager: LocationManager,
) : ViewModel() {
    private var _lastKnowLocation = MutableLiveData<Location>()
    private var _weather = MutableLiveData<CurrentWeather>()
    val weather: LiveData<CurrentWeather> = _weather

    init {
        _weather.value = null
        _lastKnowLocation.value = null
    }

    /*
     * request weather to API
     */
    fun getCurrentWeatherForLocation(lat:Double, lon:Double){
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCurrentWeatherFor(Coordinate(lat, lon)).collect {

                when(it){
                    is NetworkResult.Loading -> {
                        Log.d("weather_request_loading", "is loading: ${it.isLoading}")
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

    /*
     * request location to Location Manager
     */
    suspend fun requestForCurrentLocation(){
        locationManager.locationFlow().collect{ location ->
            // avoid request locations multiple times, at least 10 min each
           _lastKnowLocation.value?.let { lastLoc ->
               val diff = location.time - lastLoc.time // time unix epoch in millis
               val diffInMinutes = diff / 1000 / 60
               if(diffInMinutes < 0) return@let // invalid time
               if(diffInMinutes < 10) return@collect
           }
            _lastKnowLocation.value = location
           getCurrentWeatherForLocation(location.latitude, location.longitude)
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelScope.cancel()
    }



}

