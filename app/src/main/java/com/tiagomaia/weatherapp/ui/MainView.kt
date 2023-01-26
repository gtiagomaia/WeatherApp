package com.tiagomaia.weatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.tiagomaia.weatherapp.model.usecase.CurrentWeather
import com.tiagomaia.weatherapp.utils.IconCode


@Composable
fun MainView(viewModel:WeatherViewModel = hiltViewModel()) {
    // State
    val weather = viewModel.weather.observeAsState()

    // API call
    LaunchedEffect(Unit){
        viewModel.getCurrentWeatherForLocation(40.2, -8.41)
    }

    //if (weather.value == null) return

    //weather.value?.
    weather.value?.let {
        ShowCurrentWeather(it)
    }


}

@Composable
fun ShowCurrentWeather(current: CurrentWeather) {

    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        Text(current.name, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(10.dp))
        //Box(modifier = Modifier.padding(30.dp)) {
            //Image(painter = current, contentDescription = "Current Weather")
            Image(
                painter = painterResource(IconCode.getResourceByCode(current.weather.icon)),
                contentDescription = null,
                modifier = Modifier.size(60.dp).align(Alignment.CenterHorizontally)
                //.clip(CircleShape)
                //.border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
            )
            Row(modifier = Modifier.fillMaxWidth().align(Alignment.CenterHorizontally).offset(x=5.dp), horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)) {
                Text(
                    text = "${current.main.temp}", color = Color.White,
                    style = TextStyle(
                        fontSize = 40.sp,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0.0f, 0.0f),
                            blurRadius = 6f
                        )
                    ),
                    modifier = Modifier.alignByBaseline()
                )
                Text(
                    text = "ºC", color = Color.White,
                    style = TextStyle(
                        fontSize = 18.sp,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(0.0f, 0.0f),
                            blurRadius = 6f
                        )
                    ),
                    modifier = Modifier.wrapContentHeight(Alignment.Top)
                )
            }
        //}
        Text(current.weather.description, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(5.dp))
    }
}