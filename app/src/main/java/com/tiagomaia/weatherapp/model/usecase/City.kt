package com.tiagomaia.weatherapp.model.usecase


data class City (
    val id: Int,
    val name: String,
    val country: String,
    val coord: Coordinate,
    val population:Int,
    val sunrise:Int,
    val sunset:Int
)
