package com.example.fairdriveapp.Screens

import android.Manifest
import android.app.appsearch.AppSearchResult.RESULT_OK
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.fairdriveapp.MapViewModel
import com.example.fairdriveapp.components.SearchBar
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import java.util.Locale


@Composable
fun MapScreen(mapViewModel: MapViewModel) {

    //initial position
    //val initialPosition = LatLng(40.9971, 29.1007)

    //camera position state
    val cameraPositionState = rememberCameraPositionState()

    val selectedLocation by mapViewModel.selectedLocation
    val dropLocation by mapViewModel.dropLocation

    val polylinePoints = listOfNotNull(
        selectedLocation,
        dropLocation
    )

    //checking if permission is granted
    val context = LocalContext.current
    val userLocation by mapViewModel.userLocation
    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

    Column(modifier = Modifier.fillMaxSize()){
        Spacer(modifier = Modifier.height(18.dp))

        Text(text = "Pickup Location",
            modifier = Modifier.padding(start = 6.dp))
        SearchBar(
            hintTitle = "Search your pickup location",
            onPlaceSelected = {place ->
                mapViewModel.geocodeSelectedLocation(place, context)
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(text = "Drop off Location",
            modifier = Modifier.padding(start = 6.dp))
        SearchBar(
            hintTitle = "Search your drop off location",
            onPlaceSelected = {place ->
                mapViewModel.geocodeDropLocation(place, context)
            }
        )

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ){
            if(polylinePoints.size > 1){
                Polyline(
                    points = polylinePoints,
                    width = 10f
                )
            }

            selectedLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "Pickup Location",
                    snippet = "This is your pickup location"
                )
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
            }

            dropLocation?.let {
                Marker(
                    state = MarkerState(position = it),
                    title = "DropOff Location",
                    snippet = "This is your drop off location"
                )
                cameraPositionState.position = CameraPosition.fromLatLngZoom(it, 15f)
            }
        }
    }



    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ){ isGranted ->
        if(isGranted){
            mapViewModel.fetchUserLocation(context, fusedLocationProviderClient)
        }
        else{
            Toast.makeText(
                context,
                "Please go to your settings to give permission",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    LaunchedEffect(Unit) {
        when(PackageManager.PERMISSION_GRANTED){
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) -> {
                mapViewModel.fetchUserLocation(context, fusedLocationProviderClient)
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }


}




