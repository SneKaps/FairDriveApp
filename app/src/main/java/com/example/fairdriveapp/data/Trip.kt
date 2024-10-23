package com.example.fairdriveapp.data

import com.google.android.gms.maps.model.LatLng

data class Trip(
    val pickupLocation : Location,
    val dropoffLocation: Location
)
