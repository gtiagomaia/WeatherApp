package com.tiagomaia.weatherapp.ui

import androidx.compose.animation.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
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
import com.tiagomaia.weatherapp.utils.IconCode


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
                }
            )
        },
        backgroundColor = Color.Transparent,
        contentColor = Color.White,
        content = {
            DetailsWeatherContentView(Modifier.padding(it), city, lat, lng)
        }
    )

}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DetailsWeatherContentView(modifier:Modifier, content: String?, lat:Float?, lng:Float?, viewModel: WeatherViewModel = hiltViewModel()) {



    val weather = viewModel.weather.observeAsState()
    var visible by remember { mutableStateOf(true) }
    // API call
    LaunchedEffect(Unit){
        lat ?: return@LaunchedEffect
        lng ?: return@LaunchedEffect
        viewModel.getCurrentWeatherForLocation(lat.toDouble(), lng.toDouble())
        visible = true
    }

    AnimatedContent(targetState = visible, transitionSpec = { scaleIn() with fadeOut()}) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    /* going back to the main screen */

                }
            ) {
                Text(text = "Go back")
            }

            AnimatedContent(targetState = visible, transitionSpec = {slideInHorizontally() with fadeOut()}) {
                weather.value?.let {
                    ShowWeather(it)
                }
            }

        }
    }
}


@Composable
fun ShowWeather(weather: CurrentWeather){
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        Text(weather.name, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(10.dp))
        //Box(modifier = Modifier.padding(30.dp)) {
        //Image(painter = current, contentDescription = "Current Weather")
        Image(
            painter = painterResource(IconCode.getResourceByCode(weather.weather.icon)),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.CenterHorizontally)
            //.clip(CircleShape)
            //.border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
        Row(modifier = Modifier
            .fillMaxWidth()
            .align(Alignment.CenterHorizontally)
            .offset(x = 5.dp), horizontalArrangement = Arrangement.spacedBy(3.dp, Alignment.CenterHorizontally)) {
            Text(
                text = "${weather.main.temp}", color = Color.White,
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
                modifier = Modifier.wrapContentHeight(Alignment.CenterVertically)
            )
        }
        //}
        Text(weather.weather.description, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(0.dp))
    }
}
