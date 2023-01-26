package com.tiagomaia.weatherapp.model.response

data class SysInternalParamsResponse(
    val type:Int? = null, //internal param
    val id:Int? = null, //internal param
    val message:String? = null, //internal param
    val country:String? = null, // country code
    val sunrise:Int? = null, //date in unix timestamp
    val sunset:Int? = null //date in unix timestamp
)