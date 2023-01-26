package com.tiagomaia.weatherapp.model.usecase

data class SysInternalParams(
    val type:Int, //internal param
    val id:Int, // Weather condition codes
    val message:String, //internal param
    val country:String, // country code
    val sunrise:Int, //date in unix timestamp
    val sunset:Int //date in unix timestamp
)
