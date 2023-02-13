package com.lahrachtech.getcurrentlocation

import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityGPS"
    val MY_PERMISSIONS_REQUEST_LOCATION = 1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        button.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                    if (location != null) {
                        val geocoder = Geocoder(this, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                        val address = addresses?.get(0)?.getAddressLine(0)
                        val city = addresses?.get(0)?.locality
                        val country = addresses?.get(0)?.countryName
                        val latitude = addresses?.get(0)?.latitude
                        val longitude = addresses?.get(0)?.longitude
                        Log.d("Location", "Address: $address, City: $city, Country: $country")
                        Log.d(TAG, "address $address")
                        tvCity2.text = "$city"
                        tvCountry2.text = "$country"
                        tvAdress2.text = "$address"
                        tvLatitude.text = "$latitude"
                        tvLongitude.text = "$longitude"
                    } else {
                        Log.d("Location", "Could not get current location")
                    }
                }
            } else {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val foregroundOnlyLocationPermissionApproved =
                    (PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    ))
                if (foregroundOnlyLocationPermissionApproved) {
                    // foreground permission already granted
                    Toast.makeText(
                        this,
                        "foreground permission already granted",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    // request foreground location permission
                    Toast.makeText(
                        this,
                        "request foreground location permission",
                        Toast.LENGTH_SHORT
                    ).show()
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                        MY_PERMISSIONS_REQUEST_LOCATION
                    )
                }
            } else {
                // request location permission
                Toast.makeText(this, "request location permission", Toast.LENGTH_SHORT).show()
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            MY_PERMISSIONS_REQUEST_LOCATION -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    // permission was granted
                    Toast.makeText(this, "permission was granted", Toast.LENGTH_SHORT).show()

                } else {
                    // permission denied
                    Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show()

                }
                return
            }
        }
    }


}
