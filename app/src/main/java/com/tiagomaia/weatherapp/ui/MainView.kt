package com.tiagomaia.weatherapp.ui

import android.Manifest
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.*
import com.tiagomaia.weatherapp.R
import com.tiagomaia.weatherapp.extensions.readAssetsFile
import com.tiagomaia.weatherapp.model.usecase.City
import com.tiagomaia.weatherapp.model.usecase.Coordinate
import com.tiagomaia.weatherapp.model.usecase.Rain
import com.tiagomaia.weatherapp.utils.IconCode
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun MainView(navController: NavController) {

    Column (
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ){

        ShowCurrentWeather()

        ListOfCitiesView(navController)

    }

}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ShowCurrentWeather(viewModel:WeatherViewModel = hiltViewModel()) {
    // State
    // location permission state
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val coroutineScope = rememberCoroutineScope()
    val locationPermissionState = rememberMultiplePermissionsState( listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)){
        if(it.values.contains(true)){ // at least one permission was granted


        }
    }
    val animationState = remember { MutableTransitionState(true) }
    val weather = viewModel.weather.observeAsState()
    val (location, setLocation) = remember {  mutableStateOf<Coordinate?>( null) }
    SideEffect {
        locationPermissionState.launchMultiplePermissionRequest()
    }
    // API call
    LaunchedEffect(weather.value == null){
        if(locationPermissionState.permissions.map { it.status.isGranted }.contains(true)) {
            coroutineScope.launch {
                Log.i("AG", "corroutine launch")
                viewModel.requestForCurrentLocation()
            }
        }
    }

    val current = weather.value ?: return

    AnimatedVisibility(visibleState = animationState, enter = slideInVertically()) {
        Pager(
            content1 = { Page1CurrentWeather(current.name, current.weather.description, current.weather.icon, current.main.temp) },
            content2 = { Page2CurrentTemperature(current.main.feelsLike, current.main.tempMin, current.main.tempMax) },
            content3 = { Page3CurrentHumidity(current.main.humidity, current.visibility, current.rain) }
        )
    }

}

@Composable
fun Page3CurrentHumidity(humidity: Int, visibility: Int, rain: Rain) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Humidity", color= Color.White, fontSize = 14.sp, modifier = Modifier)
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp) ){
            Image(
                painter = painterResource(R.drawable.temperature),
                contentDescription = "temp",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart)
                    .alpha(0.9f)
                    .fillMaxWidth()

            )
            Text(
                text = "$humidity %", color = Color.White,
                style = TextStyle(
                    fontSize = 40.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 6f
                    )
                ),
                modifier = Modifier.align(Alignment.Center)
            )

        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$visibility KM", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
                Text("Visibility", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("$rain mm", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
                Text("Precipitation", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
            }
        }
    }
}

@Composable
fun Page2CurrentTemperature(feelsLike: Double, tempMin: Double, tempMax: Double) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text("Feels like", color= Color.White, fontSize = 14.sp, modifier = Modifier)
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp) ){
            Image(
                painter = painterResource(R.drawable.temperature),
                contentDescription = "temp",
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.CenterStart)
                    .alpha(0.9f)
                    .fillMaxWidth()

            )
            Text(
                text = "${feelsLike.roundToInt()} º", color = Color.White,
                style = TextStyle(
                    fontSize = 40.sp,
                    shadow = Shadow(
                        color = Color.Black,
                        blurRadius = 6f
                    )
                ),
                modifier = Modifier.align(Alignment.Center)
            )

        }
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${tempMin.roundToInt()}º", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
                Text("min", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
            }
            Spacer(modifier = Modifier.width(20.dp))
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${tempMax.roundToInt()}º", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
                Text("max", color= Color.White, fontSize = 13.sp, modifier = Modifier.padding(bottom = 1.dp))
            }
        }
    }
}

@Composable
fun Page1CurrentWeather(localName:String, description:String, icon:String, temperature:Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(localName, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(bottom = 10.dp))
        //Box(modifier = Modifier.padding(30.dp)) {
        //Image(painter = current, contentDescription = "Current Weather")
        Image(
            painter = painterResource(IconCode.getResourceByCode(icon)),
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
                text = "$temperature", color = Color.White,
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
        Text(description, color= Color.White, fontSize = 14.sp, modifier = Modifier.padding(0.dp))
    }
}


@Composable
fun ListOfCitiesView(navController: NavController) {
    var animationState = remember { MutableTransitionState(true) }
    val context = LocalContext.current
    val cities: List<City> =
        context.resources.assets.readAssetsFile("cities.json", Array<City>::class.java)

    Column(
        verticalArrangement = Arrangement.Top
    ) {
       cities.forEach { city ->
           AnimatedVisibility(visibleState = animationState, enter = slideInVertically()) {
               CityRow(city = city) {
                   navController.navigate("details/${city.name}/lat=${city.coord.lat}/lon=${city.coord.lon}")
               }
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



@OptIn(ExperimentalPagerApi::class)
@Composable
fun Pager(
    content1: @Composable () -> Unit,
    content2: @Composable () -> Unit,
    content3: @Composable () -> Unit
){
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(260.dp)
        .padding(20.dp)
        .background(Color.Gray.copy(0.8f), shape = RoundedCornerShape(28.dp)))  {
        val pagerState = rememberPagerState()

        // Display 10 items
        HorizontalPager(
            count = 3,
            state = pagerState,
            // Add 32.dp horizontal padding to 'center' the pages
            //contentPadding = PaddingValues(horizontal = 32.dp),
            modifier = Modifier
                //.weight(1f)
                .fillMaxWidth(),
        ) { page ->


            Box(modifier = Modifier
                .fillMaxSize()
                //.padding(16.dp)
                ) {

                when(page){
                    0 -> content1()
                    1 -> content2()
                    2 -> content3()
                }

            }
        }
        HorizontalPagerIndicator(
            pagerState = pagerState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
        )
    }
}


@Composable
internal fun PagerSampleItem(
    page: Int,
    modifier: Modifier
) {

}
