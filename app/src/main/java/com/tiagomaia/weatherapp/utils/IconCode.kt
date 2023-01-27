package com.tiagomaia.weatherapp.utils

import android.util.Log
import com.tiagomaia.weatherapp.R

// reference: https://openweathermap.org/weather-conditions#Icon-list

enum class IconCode(val code:String, val resourceId:Int){
    clearSkyDay("01d", R.drawable.sunny),
    fewCloudsDay("02d", R.drawable.cloudy_sunny),
    scatteredCloudsDay("03d", R.drawable.cloudy),
    brokenCloudsDay("04d", R.drawable.cloudy_broken),
    showerRainDay("09d", R.drawable.raining),
    rainDay("10d", R.drawable.thunder),
    thunderStormDay("11d", R.drawable.sunny),
    snowDay("13d", R.drawable.snowing_sunny),
    mistDay("50d", R.drawable.fog),
    clearSkyNight("01n", R.drawable.night),
    fewCloudsNight("02n", R.drawable.cloudy_night),
    scatteredCloudsNight("03n", R.drawable.cloudy),
    brokenCloudsNight("04n", R.drawable.cloudy_broken),
    showerRainNight("09n", R.drawable.raining),
    rainNight("10n", R.drawable.thunder),
    thunderStormNight("11n", R.drawable.night),
    snowNight("13n", R.drawable.snowing_night),
    mistNight("50n", R.drawable.fog);

    companion object {
        fun getResourceByCode(code: String):Int {
            return try {
               values().find { it.code == code }?.resourceId ?: R.drawable.cloudy_sunny
            } catch(e:Exception){
                Log.e("IconCode", "for code $code doesn't have an icon")
                R.drawable.cloudy_sunny
            }
        }

    }
}