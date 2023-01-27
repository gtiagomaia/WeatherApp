package com.tiagomaia.weatherapp.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animation
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigator
import androidx.navigation.navArgument
import androidx.navigation.navOptions
import com.tiagomaia.weatherapp.R
import com.tiagomaia.weatherapp.extensions.readAssetsFile
import com.tiagomaia.weatherapp.model.usecase.City
import com.tiagomaia.weatherapp.utils.IconCode


@Composable
fun MainView(navController: NavController) {

    Column (
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        ShowCurrentWeather()
        ListOfCitiesView(navController)
    }

}

@Composable
fun ShowCurrentWeather(viewModel:WeatherViewModel = hiltViewModel()) {
    // State
    var animationState = remember { MutableTransitionState(false) }
    val weather = viewModel.weather.observeAsState()

    // API call
    LaunchedEffect(Unit){
        viewModel.getCurrentWeatherForLocation(40.2, -8.41)
        animationState.targetState = true
    }
    val current = weather.value ?: return
    AnimatedVisibility(visibleState = animationState, enter = slideInVertically()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)) {
            Text(current.name, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(10.dp))
            //Box(modifier = Modifier.padding(30.dp)) {
            //Image(painter = current, contentDescription = "Current Weather")
            Image(
                painter = painterResource(IconCode.getResourceByCode(current.weather.icon)),
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
                    text = "ºC", color = Color.White.copy(alpha = 0.8f),
                    style = TextStyle(
                        fontSize = 11.sp,
                        shadow = Shadow(
                            color = Color.Black.copy(alpha = 0.8f),
                            offset = Offset(0.0f, 0.0f),
                            blurRadius = 6f
                        )
                    ),
                    modifier = Modifier.offset(y= 12.dp)
                )
            }
            //}
            Text(current.weather.description, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(0.dp))
        }
    }


}


@Composable
fun ListOfCitiesView(navController: NavController) {
    val context = LocalContext.current
    val cities: List<City> =
        context.resources.assets.readAssetsFile("cities.json", Array<City>::class.java)

    LazyColumn(
        modifier = Modifier,
        verticalArrangement = Arrangement.Top
    ) {
        items(cities) { city ->
            CityRow(city = city) {
                navController.navigate("details/${city.name}/lat=${city.coord.lat}/lon=${city.coord.lon}")
            }
        }
    }
}

@Composable
fun CityRow(city: City, onClick:(city:City)->Unit) {
//    val weather = viewModel.weather.observeAsState()
//    LaunchedEffect(Unit){
//        viewModel.getCurrentWeatherForLocation(city.coord)
//    }

        // Color.Gray.copy(alpha = 0.7f)
    Row{

        Button(
            onClick = { onClick(city) },
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 4.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(contentColor = Color.White, containerColor = Color.Gray.copy(alpha = 0.7f))

        ) {
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                Spacer(modifier = Modifier.width(0.dp))
                Text(
                    text = city.name,
                    color = Color.White,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Left,
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 4.dp)
                        .background(Color.Transparent)
                        .fillMaxWidth()
                )
                Icon(
                    painter = painterResource(id = R.drawable.round_arrow_forward),
                    contentDescription = "navigate to",
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .size(12.dp)
                )
            }
        }

    }
}

