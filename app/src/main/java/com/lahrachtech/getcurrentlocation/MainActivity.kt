package com.lahrachtech.getcurrentlocation

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivityGPS"
    val MY_PERMISSIONS_REQUEST_LOCATION = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button.setOnClickListener {
            requestPermission()

            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, object :
                    LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                        val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                        val city = addresses?.get(0)?.locality
                        val country = addresses?.get(0)?.countryName
                        val address = addresses?.get(0)?.getAddressLine(0)
                        val message = "Current location: $address, $city, $country"
//                        Log.d(TAG, message)
                        Log.d(TAG, "city $city")
                        Log.d(TAG, "country $country")
                        Log.d(TAG, "address $address")
                        locationManager.removeUpdates(this)
                    }

                    override fun onProviderEnabled(provider: String) {}

                    override fun onProviderDisabled(provider: String) {}

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                })
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
