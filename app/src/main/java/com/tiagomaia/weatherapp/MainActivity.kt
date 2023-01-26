package com.tiagomaia.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tiagomaia.weatherapp.ui.MainView
import com.tiagomaia.weatherapp.ui.WeatherViewModel
import com.tiagomaia.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

/* 
"cloud_night"
"raining",			
"thunder",
"cloudy",			
"raining_night",		
"thunder_night",
"cloudy_sunny",		
"rainning_sunny",		
"thunder_sunny",
"fog",				
"snowing",			
"windy",
"fog_2",			
"snowing_night",		
"windy_cloudy",
"snowing_sunny",		
"windy_night",
"night",			
"sunny",			
"windy_sunny",
*/
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel:WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")

                    MainView()

                    //ListOfImages(resources = resourcesImageName())
                }
            }
        }
    }




}

fun resourcesImageName() : List<Int> {
    return listOf(R.drawable.cloudy_night,
        R.drawable.raining,
        R.drawable.thunder,
        R.drawable.cloudy,
        R.drawable.raining_night,
        R.drawable.thunder_night,
        R.drawable.cloudy_sunny,
        R.drawable.rainning_sunny,
        R.drawable.thunder_sunny,
        R.drawable.snowing,
        R.drawable.windy,
        R.drawable.fog,
        R.drawable.snowing_night,
        R.drawable.windy_cloudy,
        R.drawable.snowing_sunny,
        R.drawable.windy_night,
        R.drawable.night,
        R.drawable.sunny,
        R.drawable.windy_sunny)
}

@Composable
fun ListOfImages(resources: List<Int>) {
    LazyColumn {
        items(resources) { res ->
            RowImage(resourceImage = res)
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Composable
fun RowImage(resourceImage:Int) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(resourceImage),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
            //.clip(CircleShape)
            //.border(1.5.dp, MaterialTheme.colors.secondary, CircleShape)
        )
    }

}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        //Greeting("Android")
        ListOfImages(resources = listOf(R.drawable.fog))
    }
}