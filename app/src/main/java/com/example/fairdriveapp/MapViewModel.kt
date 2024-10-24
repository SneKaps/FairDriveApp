package com.example.fairdriveapp

import android.content.Context
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapViewModel :ViewModel(){

    //state to store user location
    private val _userLocation = mutableStateOf<LatLng?>(null)
    val userLocation: State<LatLng?> = _userLocation

    //val database: DatabaseReference = FirebaseDatabase.getInstance().reference

    //state to store selected location
    private val _pickLocation = mutableStateOf<LatLng?>(null)
    val pickLocation: State<LatLng?> = _pickLocation

    private val _dropLocation = mutableStateOf<LatLng?>(null)
    val dropLocation : State<LatLng?> = _dropLocation

    fun fetchUserLocation(context: Context, fusedLocationClient: FusedLocationProviderClient) {

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val userLatLng = LatLng(location.latitude, location.longitude)
                    _userLocation.value = userLatLng
                }
            }
        }
    }

    //function to geocode selection location and update its state
    fun geocodeSelectedLocation(selectedPlace: String, context: Context){
        viewModelScope.launch {
            val geocoder = Geocoder(context)
            val addresses = withContext(Dispatchers.IO){
                geocoder.getFromLocationName(selectedPlace, 1)
            }
            if(!addresses.isNullOrEmpty()){
                val address = addresses[0]
                val latlng = LatLng(address.latitude, address.longitude)
                _pickLocation.value = latlng
            }
            else{
                "Address not found"
            }
        }

    }

    //function to geocode drop location and update its state
    fun geocodeDropLocation(dropPlace: String, context: Context){
        viewModelScope.launch {
            val geocoder = Geocoder(context)
            val addresses = withContext(Dispatchers.IO){
                geocoder.getFromLocationName(dropPlace, 1)
            }
            if(!addresses.isNullOrEmpty()){
                val address = addresses[0]
                val latlng = LatLng(address.latitude, address.longitude)
                _dropLocation.value = latlng
            }
            else{
                "Address not found"
            }
        }

    }
}