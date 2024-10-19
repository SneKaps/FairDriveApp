package com.example.fairdriveapp.Screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.fairdriveapp.AuthState
import com.example.fairdriveapp.AuthViewModel


@Composable
fun LoginScreen(modifier:Modifier, navController: NavController, authViewModel: AuthViewModel){

    var email by remember{
        mutableStateOf("")
    }

    var pwd by remember {
        mutableStateOf("")
    }
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when(authState.value){
            is AuthState.authenticated->navController.navigate("home")
            is AuthState.Error-> Toast.makeText(context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_LONG).show()
            else->Unit
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Login",
            modifier = Modifier.padding(5.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )

        OutlinedTextField(value = email,
            onValueChange = {
                email = it
            },
            label= {Text("Email")}
        )

        OutlinedTextField(value = pwd,
            onValueChange = {
                pwd = it
            },
            label= {Text("Password")}
        )

        Button( modifier = Modifier.padding(5.dp),
            onClick = {
                authViewModel.login(email,pwd)
            }) {
            Text(text = "Login")
        }


        Text("Don't have an account?",
                fontSize = 12.sp)
        TextButton(onClick = {
            navController.navigate("signup")
        }) {
            Text(text = "Signup", fontSize = 12.sp)
        }




    }
}

@Preview
@Composable
fun LoginScreenPreview(){

}


