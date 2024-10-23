package com.example.fairdriveapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fairdriveapp.ui.theme.FairDriveAppTheme
import com.example.fairdriveapp.utils.ManifestUtils
import com.google.android.libraries.places.api.Places

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyDkUZs7He4lGYBd-25mdt_6NgQcSsLwWgI")
        }
        enableEdgeToEdge()
        installSplashScreen()
        setContent {
            //val viewModel: LocationViewModel = viewModel()
            val authViewModel : AuthViewModel = viewModel()
            val mapViewModel : MapViewModel = viewModel()
            FairDriveAppTheme {
                Navigation(authViewModel, mapViewModel)
            }
        }
    }
}

