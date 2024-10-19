package com.example.fairdriveapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat

@Composable
fun LocPermission(){

    //checking for previously granted required permissions

    val context = LocalContext.current
    val locPermissionAlreadyGranted = ContextCompat.checkSelfPermission(
        context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    //requesting permissions

    val locationPermission = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions->
            val permissionsGranted = permissions.values.reduce{ acc, isPermissionGranted ->
                acc && isPermissionGranted
            }
            if(!permissionsGranted){
                //permission not granted
            }
        }
    )
    //requesting to launch activity
    locationPermissionLauncher.launch(locationPermission)

    //explaining why permission needed
    val activity = context as? Activity
    val shouldShowPermissionRationale: Boolean = activity?.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION) ?: false
}


@Composable
fun ShowPermissionRationale(){

    AlertDialog(
        onDismissRequest = { /*TODO when dismiss happens*/ },
        title = { "Permission Required" },
        text = { "Permission needed to find your location" },
        confirmButton = {
            TextButton(onClick = { /*TODO when user gives permission*/ }) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(onClick = { /*TODO when user denies to give permission*/ }) {
                Text("Deny")
            }
        }
    )
}
