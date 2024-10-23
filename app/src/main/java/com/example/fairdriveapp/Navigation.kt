package com.example.fairdriveapp

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fairdriveapp.Screens.HomeScreen
import com.example.fairdriveapp.Screens.LoginScreen
import com.example.fairdriveapp.Screens.RideScreen
import com.example.fairdriveapp.Screens.SignupScreen

@Composable
fun Navigation(authViewModel: AuthViewModel,
               mapViewModel: MapViewModel){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login"){
        composable("login"){
            LoginScreen(Modifier, navController, authViewModel)
        }
        composable("signup"){
            SignupScreen(Modifier, navController, authViewModel)
        }
        composable("home") {
            HomeScreen(Modifier, navController, authViewModel, mapViewModel)
        }
        composable("ride"){
            RideScreen()
        }
    }


}