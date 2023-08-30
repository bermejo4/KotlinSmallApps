package com.example.locationgps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    private val BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE = 2
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var latitud: TextView
    private lateinit var longitud: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check if the app has location permissions
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getLastLocation()
        } else {
            // Request foreground location permission
            requestForegroundLocationPermission()
        }

        latitud = findViewById(R.id.Latitud)
        longitud = findViewById(R.id.Longitud)

    }

    private fun getLastLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
                    if (location != null) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        //val message = "Latitud: $latitude, Longitud: $longitude"
                        //Toast.makeText(this, message, Toast.LENGTH_LONG).show()
                        latitud.text = "Latitud $latitude"
                        longitud.text = "Longitud $longitude"
                    } else {
                        Toast.makeText(this, "Ubicación no disponible", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun requestForegroundLocationPermission() {
        val permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted, get the last location
                    getLastLocation()
                } else {
                    // Permission denied, show a message or take some other action
                    Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
                }
            }

        // Request the ACCESS_FINE_LOCATION permission for foreground access
        permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    @RequiresApi(30)
    private fun requestBackgroundLocationPermission() {
        val backgroundPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission granted, get the last location
                    getLastLocation()
                } else {
                    // Permission denied, show a message or take some other action
                    Toast.makeText(
                        this,
                        "Permiso de ubicación en segundo plano denegado",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        // Request the ACCESS_BACKGROUND_LOCATION permission for background access (Android 11+)
        backgroundPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Foreground location permission granted, check if background location permission is required
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_BACKGROUND_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        // Background location permission already granted, get the last location
                        getLastLocation()
                    } else {
                        // Request background location permission
                        requestBackgroundLocationPermission()
                    }
                } else {
                    // Android version is below 11 (not required to request background permission)
                    getLastLocation()
                }
            } else {
                // Foreground location permission denied, show a message or take some other action
                Toast.makeText(this, "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == BACKGROUND_LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Background location permission granted, get the last location
                getLastLocation()
            } else {
                // Background location permission denied, show a message or take some other action
                Toast.makeText(
                    this,
                    "Permiso de ubicación en segundo plano denegado",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
