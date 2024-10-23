package com.example.fairdriveapp.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fairdriveapp.AuthState
import com.example.fairdriveapp.AuthViewModel
import com.example.fairdriveapp.MapViewModel

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    mapViewModel: MapViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current


    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.unauthenticated->navController.navigate("login")
            else -> Unit

        }
    }
    //MapScreen( mapViewModel)

    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        //Text("Welcome to FairDrive", fontSize = 25.sp)

        //Spacer(modifier = Modifier.height(10.dp))
        //Button(onClick = {
            //authViewModel.signOut()
        //}) {
            //Text(text = "SignOut")
        //}
        Spacer(modifier = Modifier.height(10.dp))
        MapScreen( mapViewModel, navController)

        //LocationDisplay(locationUtils,viewModel,context)
    }



}

@Preview
@Composable
fun HomeScreenPreview(){

}
