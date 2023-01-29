package com.tiagomaia.weatherapp

import android.util.Log
import com.tiagomaia.weatherapp.model.response.CurrentWeatherResponse
import com.tiagomaia.weatherapp.model.usecase.Coordinate
import com.tiagomaia.weatherapp.network.ApiInterface
import com.tiagomaia.weatherapp.network.NetworkResult
import com.tiagomaia.weatherapp.network.OWMRepo
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Inject
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.net.HttpURLConnection


@HiltAndroidTest
class ApiTest {

    @Inject
    lateinit var repo:OWMRepo
    lateinit var mockApiInterface: ApiInterface
    //private val mockApiService = mock<ApiInterface>()
    private val lat = 40.2
    private val lon = -8.41

    @Before
    fun setup() {
        // Create a mock implementation of the ApiService
        mockApiInterface = mock(ApiInterface::class.java)
        // Create an instance of the Repo class and inject the mock ApiService
        repo = OWMRepo(mockApiInterface)

    }

    @Test
    fun `getWeather should emit Loading and Success`() = runBlocking(Dispatchers.IO) {

        //val weatherResponse = CurrentWeatherResponse()
        val weatherResponse = mock(CurrentWeatherResponse::class.java)
        val weatherModel = weatherResponse.toModel()

        `when`(mockApiInterface.getCurrentWeatherFor(lat, lon)).thenReturn(weatherResponse)
        println(weatherResponse)

        val result = repo.getCurrentWeatherFor(Coordinate(lat, lon)).toList()
        println(result)

        assertEquals(2, result.size) //loading, success or loading, error

        val success = result.filter { it is NetworkResult.Success }.first() as? NetworkResult.Success
        assertEquals(NetworkResult.Success(weatherResponse), result.last())

        /*
        assertNotNull(NetworkResult.Success(weatherResponse))

        assertEquals(200, weatherResponse.cod) //NetworkResult.Loading(isLoading = true)

        assertEquals(result, NetworkResult.Success(weatherModel)) //NetworkResult.Loading(isLoading = true)
        assertNotNull(result)

        val useCase = result as? NetworkResult.Success
        assertNotNull(useCase?.data) */

    }

    @Test
    fun `test result http request of the api`(){
        val weatherResponse = mock(CurrentWeatherResponse::class.java)
        val apiInterface:ApiInterface = mockk()
        //Mock the getWeather function
        coEvery { apiInterface.getCurrentWeatherFor(lat, lon) } returns weatherResponse

        //call the function
        runBlocking {
            val result = apiInterface.getCurrentWeatherFor(lat, lon)
            coVerify { apiInterface.getCurrentWeatherFor(lat, lon) }

            assertNotNull(result)
            assertEquals(HttpURLConnection.HTTP_OK, result.cod)
        }

    }

}