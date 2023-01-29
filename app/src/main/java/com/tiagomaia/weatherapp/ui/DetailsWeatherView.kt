package com.tiagomaia.weatherapp.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.tiagomaia.weatherapp.R
import com.tiagomaia.weatherapp.model.usecase.CurrentWeather
import com.tiagomaia.weatherapp.utils.DateFormat
import com.tiagomaia.weatherapp.utils.IconCode
import java.util.*
import kotlin.math.roundToInt


@Composable
fun DetailsWeatherView(navController: NavController, city: String?, lat:Float?, lng:Float?) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = { Text(text = "$city") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ){
                        Icon(
                            painter = painterResource(id = R.drawable.round_arrow_forward),
                            contentDescription = "Go back",
                            modifier = Modifier.rotate(180f),
                            tint = Color.White
                        )
                    }
                },
                backgroundColor = Color.Transparent,
                elevation = 0.dp
            )
        },
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        content = {
            DetailsWeatherContentView(Modifier.padding(it).fillMaxSize().verticalScroll(rememberScrollState()), city, lat, lng)
        }
    )

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailsWeatherContentView(modifier:Modifier, content: String?, lat:Float?, lng:Float?, viewModel: WeatherViewModel = hiltViewModel()) {



    val weather = viewModel.weather.observeAsState()
    val visible = remember { MutableTransitionState(true) }
    // API call
    LaunchedEffect(Unit){
        lat ?: return@LaunchedEffect
        lng ?: return@LaunchedEffect
        viewModel.getCurrentWeatherForLocation(lat.toDouble(), lng.toDouble())
    }

    AnimatedVisibility(visibleState = visible, enter = scaleIn()) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))

             weather.value?.let {

                 DateText(it.dt)
                 Spacer(modifier = Modifier.height(5.dp))
                 ShowWeather(it)
                 Spacer(modifier = Modifier.height(16.dp))
                 Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth().wrapContentHeight().padding(horizontal = 20.dp)){

                     ShowTextAlignedToStart("${it.name}, ${it.sys.country}")
                     LineSeparator()
                     ShowTextAlignedToStart("Minimum temperature: ${it.main.tempMin.roundToInt()}ºC")
                     ShowTextAlignedToStart("Maximum temperature: ${it.main.tempMax.roundToInt()}ºC")
                     LineSeparator()
                     ShowTextAlignedToStart("Humidity: ${it.main.humidity}%")
                     ShowTextAlignedToStart("Visibility: ${it.visibility} KM")
                     ShowTextAlignedToStart("Clouds: ${it.clouds.all}%")
                     ShowTextAlignedToStart("Atmosphere pressure: ${it.main.pressure} hPa")
                     LineSeparator()

                     Row (verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(start = 20.dp)) {
                         Text(text = "Wind: ${it.wind.speed.roundToInt()} KM/h")
                         Icon(painter = painterResource(id = R.drawable.arrow), contentDescription = "direction", modifier = Modifier.rotate(it.wind.deg.toFloat()).size(24.dp).padding(2.dp))
                     }
                     LineSeparator()
                     ShowTextAlignedToStart("Rain in last 1 hour: ${it.rain.last1h} mm")
                     ShowTextAlignedToStart("Rain in last 3 hour: ${it.rain.last3h} mm")
                     ShowTextAlignedToStart("Snow in last 1 hour: ${it.rain.last1h} mm")
                     ShowTextAlignedToStart("Snow in last 3 hour: ${it.rain.last3h} mm")
                     LineSeparator()
                     ShowTextAlignedToStart("Sunrise: ${DateFormat.getEpochTimeInHumanReadableOnlyTime(it.sys.sunrise.toLong())}")
                     ShowTextAlignedToStart("Sunset: ${DateFormat.getEpochTimeInHumanReadableOnlyTime(it.sys.sunset.toLong())}")
                 }


             }

        }
    }
}

@Composable
fun ShowTextAlignedToStart(text:String) {
    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier
        .fillMaxWidth()
        .padding(start = 20.dp)) {
        Text(text = text)
    }
}

@Composable
fun LineSeparator() {
    Spacer(modifier = Modifier
        .fillMaxWidth()
        .height(1.dp)
        .background(Color.Gray)
        .padding(horizontal = 20.dp, vertical = 30.dp))
}

@Composable
fun DateText(unixTime: Int) {
    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier
        .fillMaxWidth()
        .padding(end = 20.dp)) {
        Text(text = DateFormat.getEpochTimeInHumanReadable(unixTime.toLong()))
    }
}


@Composable
fun ShowWeather(weather: CurrentWeather){
    Box(modifier = Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .padding(horizontal = 20.dp)
        .background(Color.Gray.copy(0.8f), shape = RoundedCornerShape(28.dp))) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Row (horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)){

                Column(verticalArrangement = Arrangement.spacedBy(5.dp, Alignment.CenterVertically),modifier = Modifier.align(Alignment.CenterVertically)){
                    Text(text = "Feels like ${weather.main.feelsLike.roundToInt()}ºC", color = Color.White)
                    Text(text = weather.weather.description.replaceFirstChar { it.uppercaseChar() }, color = Color.White)
                }

                Spacer(modifier = Modifier.width(16.dp))

                Image(
                    painter = painterResource(IconCode.getResourceByCode(weather.weather.icon)),
                    contentDescription = null,
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "${weather.main.temp}", color = Color.White,
                    style = TextStyle(
                        fontSize = 40.sp,
                        shadow = Shadow(
                            color = Color.Black,
                            blurRadius = 6f
                        )
                    ),
                    modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
                )

                Text(
                    text = "ºC", color = Color.White,
                    style = TextStyle(
                        fontSize = 11.sp,
                        shadow = Shadow(
                            color = Color.Black,
                            blurRadius = 6f
                        )
                    ),
                    modifier = Modifier
                        .wrapContentHeight(Alignment.CenterVertically)
                        .offset(y = 12.dp)
                )

            }

            Spacer(modifier = Modifier
                .height(1.dp)
                .background(Color.Gray)
                .padding(10.dp))
        }
    }
}
