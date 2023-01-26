package com.tiagomaia.weatherapp.model.response

import com.google.gson.annotations.SerializedName

data class RainOrSnowResponse(
    @SerializedName("1h") val last1h :Double? = null,
    @SerializedName("3h") val last3h :Double? = null
)