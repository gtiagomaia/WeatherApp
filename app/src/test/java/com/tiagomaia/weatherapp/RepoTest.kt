package com.tiagomaia.weatherapp

import com.google.gson.Gson
import com.tiagomaia.weatherapp.model.response.CurrentWeatherResponse
import com.tiagomaia.weatherapp.model.usecase.Coordinate
import com.tiagomaia.weatherapp.network.ApiInterface
import com.tiagomaia.weatherapp.network.OpenWeatherMapApi
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection


class RepoTest {
    lateinit var mockWebServer: MockWebServer
    lateinit var apiService: ApiInterface
    lateinit var gson: Gson
    private val lat = 40.2
    private val lon = -8.41
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        gson = Gson()
        mockWebServer = MockWebServer()
        apiService = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build().create(ApiInterface::class.java)
    }


    @Test
    fun `mock weather api test`() = runBlocking {
        val configurationResponse = Mockito.mock(CurrentWeatherResponse::class.java)
        val jsonInString: String = Gson().toJson(configurationResponse)

        val mockResponse = MockResponse()
        mockWebServer.enqueue(mockResponse.setResponseCode(HttpURLConnection.HTTP_OK)
            .setBody("{}"))

        val response = apiService.getCurrentWeatherFor(lat, lon)
        val request = mockWebServer.takeRequest()
        println(request)
        println(response)
        assertEquals("POST", request.method)
        assertNotNull(response)

    }

    @After
    fun shutdown() {
        mockWebServer.shutdown()
    }




}