package com.example.fairdriveapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.util.Locale



class LocationUtils(
    context: Context
) {
    private val _fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    fun requestLocationUpdates(viewModel: LocationViewModel){
        val locationCallback = object: LocationCallback(){
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                locationResult.lastLocation?.let {
                    val location = LocationData(lat = it.latitude, long = it.longitude)
                    viewModel.updateLocation(location)
                }
            }
        }

        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,1000).build()

        _fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    //checks if we already have permission or not
    fun hasLocationPermission(context: Context):Boolean{

        if(ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED
            &&
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_COARSE_LOCATION)==PackageManager.PERMISSION_GRANTED){
            return true
        }
        else{
            return false
        }
    }

    //to convert lat and long into proper address

    fun reverseGeoDecodeLocation(context: Context, locationData: LocationData) :String{
        //val geocoder = Geocoder(context, Locale.getDefault())
        val geocoder = Geocoder(context, Locale.getDefault())
        val coordinate = LatLng(
            locationData.lat, locationData.long
        )
        val addresses : MutableList<Address>? =
            geocoder.getFromLocation(coordinate.latitude, coordinate.longitude, 1)
        return if (addresses?.isNotEmpty() == true){
            addresses[0].getAddressLine(0)
        }
        else{
            "Address not found"
        }
    }
}