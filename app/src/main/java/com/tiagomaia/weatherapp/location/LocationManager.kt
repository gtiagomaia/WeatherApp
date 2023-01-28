package com.tiagomaia.weatherapp.location

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.Task
import com.tiagomaia.weatherapp.model.usecase.Coordinate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@SuppressLint("MissingPermission")
class LocationManager @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val TAG:String = "locationManager"
) {

    private val locationRequest by lazy {
        LocationRequest.create().also {
            it.priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
            //it.numUpdates = 3
            it.fastestInterval = TimeUnit.SECONDS.toMillis(5)
            it.interval = TimeUnit.SECONDS.toMillis(0)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates(callback: LocationCallback): Task<Void> {
        Log.d(TAG, "start location updates")
        return fusedLocationClient.requestLocationUpdates(
            locationRequest,
            callback,
            Looper.getMainLooper()
        )
    }

    private fun stopLocationUpdates(callback: LocationCallback) {
        fusedLocationClient.removeLocationUpdates(callback)
        Log.d(TAG, "stop location updates")
    }


    private fun hasPermission(): Boolean = ContextCompat.checkSelfPermission(
        fusedLocationClient.applicationContext, Manifest.permission.ACCESS_COARSE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            fusedLocationClient.applicationContext, Manifest.permission.ACCESS_FINE_LOCATION,
    ) == PackageManager.PERMISSION_GRANTED



    private fun checkClientSettings(){
        val settingsClient = LocationServices.getSettingsClient(fusedLocationClient.applicationContext)
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        settingsClient.checkLocationSettings(locationSettingsRequest).apply {
            addOnSuccessListener {
                Log.d(TAG, "settingsClient: isLocationPresent: ${it.locationSettingsStates?.isLocationPresent}")
                Log.d(TAG, "settingsClient: isLocationUsable: ${it.locationSettingsStates?.isLocationUsable}")
            }
            addOnFailureListener { Log.e(TAG, "settingsClient failed: $it")  }
        }
    }

    suspend fun locationFlow() = callbackFlow {

        //Create LocationSettingsRequest object using location request
        checkClientSettings()

        val callBack = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val location = locationResult.lastLocation ?: locationResult.locations.lastOrNull() ?: return
                val coordinate = Coordinate(
                    lat = location.latitude,
                    lon = location.longitude
                )
                try {
                    this@callbackFlow.trySend(coordinate).isSuccess
                    stopLocationUpdates(this)
                } catch (e: Exception) {
                    Log.e(TAG, "${e.message}")
                }
            }
        }

        val sendCoordinate : (Coordinate) -> Unit = {
            try {
                this@callbackFlow.trySend(it).isSuccess
                stopLocationUpdates(callBack)
            } catch (e: Exception) {
                Log.e(TAG, "${e.message}")
            }
        }


        if (!hasPermission()) return@callbackFlow
        stopLocationUpdates(callBack)
        startLocationUpdates(callBack)
            .addOnFailureListener { e ->
                Log.e(TAG, "failure on locations update: $e")
                close(e)
            }

        val tokenCancellation = CancellationTokenSource().token
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, tokenCancellation)
            .addOnSuccessListener {
                it ?: return@addOnSuccessListener
                sendCoordinate(Coordinate(it.latitude, it.longitude))
            }
            .addOnFailureListener { Log.e(TAG, "Current location failure: $it") }

        fusedLocationClient.lastLocation.addOnSuccessListener {
            it ?: return@addOnSuccessListener
            sendCoordinate(Coordinate(it.latitude, it.longitude))
        }.addOnFailureListener { Log.e(TAG, "Last location failure: $it") }

        //sendCoordinate(Coordinate(40.203314, -8.410257))
        awaitClose {
            stopLocationUpdates(callBack)
        }
    }

}
