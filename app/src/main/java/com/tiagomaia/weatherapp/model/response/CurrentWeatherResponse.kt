package com.tiagomaia.weatherapp.model.response

import com.tiagomaia.weatherapp.model.usecase.*
import kotlin.math.roundToInt


data class CurrentWeatherResponse(
    val coord: CoordinateResponse? = null,
    val weather:List<MainWeatherResponse>? = null,
    val base:String? = null,
    val main: MainResponse? = null,
    val visibility: Int? = null, // in KM
    val wind: WindResponse? = null,
    val clouds: CloudsResponse? = null,
    val rain: RainOrSnowResponse? = null,
    val snow: RainOrSnowResponse? = null,
    val dt:Int? = null, // unix timestamp
    val sys: SysInternalParamsResponse? = null,
    val timezone: Int? = null,
    val id:Int? = null,
    val name:String? = null, // name of city
    val cod:Int = 404
) {

    private fun CoordinateResponse.toModel() = Coordinate(lat ?: 0.0, lon ?: 0.0)
    private fun MainWeatherResponse.toModel() = MainWeather(id ?: 0, main ?: "", description ?: "", icon ?:"")
    private fun List<MainWeatherResponse>.toModel() = mapNotNull { it.toModel() }
    private fun MainResponse.toModel() = CurrentWeather.Main(
        temp = temp?.roundToInt() ?: 0,
        pressure = pressure ?: 0,
        humidity = humidity ?: 0,
        feelsLike = feelsLike ?: 0.0,
        tempMin = tempMin ?: 0.0,
        tempMax = tempMax ?: 0.0,
        seaLevel = seaLevel ?: 0,
        groundLevel = groundLevel ?: 0
    )
    //meter/sec to km/h
    private fun WindResponse.toModel() = Wind(((this.speed ?: 0).toInt() * 3.6),this.deg ?: 0, this.gust ?: 0.0)
    private fun CloudsResponse.toModel() = Clouds(this.all ?: 0)
    private fun RainOrSnowResponse.toRainModel() = Rain(this.last1h ?: 0.0, this.last3h ?: 0.0)
    private fun RainOrSnowResponse.toSnowModel() = Snow(this.last1h ?: 0.0, this.last3h ?: 0.0)
    private fun SysInternalParamsResponse.toModel() = SysInternalParams(type ?: 0,id ?: 0,message  ?: "", country ?: "", sunrise ?: 0, sunset ?: 0)

    fun toModel() = CurrentWeather(
        coord = coord?.toModel() ?: Coordinate(0.0,0.0),
        weather = weather?.toModel()?.firstOrNull() ?: MainWeather(0, "", "", ""),
        base = base ?: "",
        main = main?.toModel() ?: CurrentWeather.Main(0,0,0,0.0,0.0,0.0,0,0),
        visibility = (this.visibility ?: 1000) / 1000, // meter to km
        wind = wind?.toModel() ?: Wind(0.0, 0, 0.0),
        rain = rain?.toRainModel() ?: Rain(0.0, 0.0),
        snow = snow?.toSnowModel() ?: Snow(0.0, 0.0),
        dt = dt ?: 0,
        sys = sys?.toModel() ?: SysInternalParams(0,0,"", "", 0, 0),
        timezone = timezone ?: 0,
        id = id ?: -1,
        name = name ?: ""
    )

    override fun toString(): String {
        return "CurrentWeatherResponse(coord=$coord, weather=$weather, base=$base, main=$main, visibility=$visibility, wind=$wind, clouds=$clouds, rain=$rain, snow=$snow, dt=$dt, sys=$sys, timezone=$timezone, id=$id, name=$name, cod=$cod)"
    }


}


fun CoordinateResponse.toString():String {
    return "CoordinateResponse(lat=$lat, lon=$lon)"
}
private fun MainWeatherResponse.toString():String {
    return "MainWeatherResponse(id=$id, main=$main, description=$description, icon=$icon)"
}

private fun MainResponse.toString():String {
    return "MainResponse(temp=$temp, pressure=$pressure, humidity=$humidity, feelsLike=$feelsLike, tempMin=$tempMin, tempMax=$tempMax, seaLevel=$seaLevel, groundLevel=$groundLevel)"
}
private fun WindResponse.toString():String {
    return "WindResponse(speed=$speed, deg=$deg, gust=$gust)"
}
private fun CloudsResponse.toString():String {
    return "CloudsResponse(all=$all)"
}
private fun RainOrSnowResponse.toString():String{
    return "RainOrSnowResponse(last1h=$last1h, last3h=$last3h)"
}

private fun SysInternalParamsResponse.toString():String {
    return "SysInternalParamsResponse(type=$type, id=$id, message=$message, country=$country, sunrise=$sunrise, sunset=$sunset)"
}
