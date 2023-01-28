package com.tiagomaia.weatherapp



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.tiagomaia.weatherapp.ui.DetailsWeatherView
import com.tiagomaia.weatherapp.ui.MainView
import com.tiagomaia.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(modifier = Modifier.background(
                        Brush.linearGradient(
                                0.0f to Color(0xFF3C3D3D),
                                500.0f to Color(0xFF212427),
                                start = Offset.Zero,
                                end = Offset.Infinite
                        ))) {
                        NavHostView()
                    }

                }
            }
        }
    }
}


@Composable
fun NavHostView(navController: NavHostController = rememberNavController(),
                startDestination:String = "main"){
    NavHost(navController = navController, startDestination = startDestination) {
        composable("main") { MainView(navController) }
        composable("details/{city}/lat={lat}/lon={lon}", arguments = listOf(
            navArgument("city") { type = NavType.StringType; defaultValue = "N/A" },
            navArgument("lat") { type = NavType.FloatType; defaultValue = 0.0f },
            navArgument("lon") {  type = NavType.FloatType; defaultValue = 0.0f },
            )
        ) {
            DetailsWeatherView(
                navController,
                it.arguments?.getString("city"),
                it.arguments?.getFloat("lat"),
                it.arguments?.getFloat("lon")
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {

    }
}