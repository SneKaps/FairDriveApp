package com.example.fairdriveapp

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.Manifest
import android.widget.Toast
import androidx.core.app.ActivityCompat

@Composable
fun LocationDisplay(
    locationUtils: LocationUtils,
    viewModel: LocationViewModel,
    context: Context
){
    val location = viewModel.location.value

    val address = location?.let{
        locationUtils.reverseGeoDecodeLocation(context, location)
    }


    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            if(permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
                &&
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true){
                //Have access what to do
                locationUtils.requestLocationUpdates(viewModel)
            }
            else{
                //ask for access
                val rationaleRequired = ActivityCompat.shouldShowRequestPermissionRationale(
                    context as MainActivity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) || ActivityCompat.shouldShowRequestPermissionRationale(
                            context as MainActivity,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                )
                if(rationaleRequired){
                    Toast.makeText(
                        context,
                        "Permission required to book the cab",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else{
                    Toast.makeText(
                        context,
                        "Please go to your settings to give permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    )
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        if(location != null){
            Text(
                "Pickup location is: ${location.lat}, ${location.long} \n $address",
                modifier = Modifier.padding(5.dp)
            )
        }
        else {
            Text(
                "Pickup location not available",
                modifier = Modifier.padding(5.dp)
            )
        }
        
        Button(onClick = {
            if(locationUtils.hasLocationPermission(context)){
                //Permission granted to update the location
                locationUtils.requestLocationUpdates(viewModel)
            }
            else{
                //Request Location
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }){
            Text(text = "Get Location")
        }
        
    }
}