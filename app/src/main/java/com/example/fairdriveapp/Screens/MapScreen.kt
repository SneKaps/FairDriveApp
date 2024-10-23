package com.example.fairdriveapp.Screens

import android.Manifest

import android.content.Context

import android.content.pm.PackageManager
import android.location.Geocoder
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

import androidx.compose.runtime.getValue

import androidx.compose.runtime.remember

import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.fairdriveapp.MapViewModel
import com.example.fairdriveapp.components.SearchBar
import com.example.fairdriveapp.data.Location
import com.example.fairdriveapp.data.Trip
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


import com.google.firebase.database.DatabaseReference

import com.google.maps.android.compose.GoogleMap

import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState



@Composable
fun MapScreen(mapViewModel: MapViewModel, navController: NavController) {

    //initial position
    //val initialPosition = LatLng(40.9971, 29.1007)

    //camera position state
    val cameraPositionState = rememberCameraPositionState()

    val pickLocation by mapViewModel.pickLocation
    val dropLocation by mapViewModel.dropLocation

    val polylinePoints = listOfNotNull(
        pickLocation,
        dropLocation
    )

    //checking if permission is granted
    val context = LocalContext.current
    val userLocation by mapViewModel.userLocation
    val fusedLocationProviderClient =
        remember { LocationServices.getFusedLocationProviderClient(context) }

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

    Column(modifier = Modifier.fillMaxSize()){
        Spacer(modifier = Modifier.height(18.dp))

        Text(text = "Pickup Location",
            modifier = Modifier.padding(6.dp))
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
            modifier = Modifier.weight(1f).fillMaxWidth(),
            cameraPositionState = cameraPositionState
        ){
            if(polylinePoints.size > 1){
                Polyline(
                    points = polylinePoints,
                    width = 10f
                )
            }

            pickLocation?.let {
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
        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = {

                saveTripToFirebase(pickLocation, dropLocation, context, mapViewModel.database)
                navController.navigate("ride")
            }
        ){
            Text("Submit")
        }
    }
}

fun saveTripToFirebase(
    pickupLocation: LatLng?,
    dropoffLocation: LatLng?,
    context: Context,
    database: DatabaseReference
){
    if(pickupLocation == null || dropoffLocation == null){
        Toast.makeText(context, "Select both prickup and dropoff location", Toast.LENGTH_LONG).show()
        return
    }

    val geocoder = Geocoder(context)
    val pickupAddress = geocoder.getFromLocation(pickupLocation.latitude, pickupLocation.longitude, 1)
    val dropoffAddress = geocoder.getFromLocation(dropoffLocation.latitude, dropoffLocation.longitude,1)

    val pickupLoc = pickupAddress?.get(0)?.let {
        Location(
        latitude = pickupLocation.latitude,
        longitude = pickupLocation.longitude,
        address = it.getAddressLine(0)
    )
    }

    val dropoffLoc = dropoffAddress?.get(0)?.let {
        Location(
        latitude = dropoffLocation.latitude,
        longitude = dropoffLocation.longitude,
        address = it.getAddressLine(0)
    )
    }

    val trip = pickupLoc?.let {
        if (dropoffLoc != null) {
            Trip(
                pickupLocation = it,
                dropoffLocation = dropoffLoc
            )
        }
    }

    val tripId = database.child("trips").push().key
    if (tripId != null){
        database.child("trips").child(tripId).setValue(trip)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    Toast.makeText(context,"Trip saved successfully", Toast.LENGTH_LONG).show()
                }
                else{
                    Toast.makeText(context, "Failed to save trip", Toast.LENGTH_LONG).show()
                }
            }
    }
}




