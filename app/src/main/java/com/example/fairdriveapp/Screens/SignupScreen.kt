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
fun SignupScreen(modifier: Modifier,navController: NavController,authViewModel: AuthViewModel){

    /*
    var full_name by remember{
        mutableStateOf("")
    }

    var phno by remember {
        mutableStateOf("")
    }
    */

    var email by remember {
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
                (authState.value as AuthState.Error).message,Toast.LENGTH_LONG).show()
            else->Unit
        }
    }




    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Signup",
            modifier = Modifier.padding(5.dp),
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontStyle = FontStyle.Normal
        )
/*
        OutlinedTextField(value = full_name,
            onValueChange = {
                full_name = it
            },
            label= { Text("Full Name") }
        )

        OutlinedTextField(value = phno,
            onValueChange = {
                phno = it
            },
            label= { Text("Phone Number") }
        )
*/
        OutlinedTextField(value = email,
            onValueChange = {
                email = it
            },
            label= { Text("Email") }
        )

        OutlinedTextField(value = pwd,
            onValueChange = {
                pwd = it
            },
            label= { Text("Password") }
        )

        Button( modifier = Modifier.padding(5.dp),
            onClick = {
                authViewModel.signup(email, pwd)
            }) {
            Text(text = "Create Account")
        }

        Text("Already have an account?",
            fontSize = 12.sp)
        TextButton(onClick = {
            navController.navigate("login")
        }) {
            Text(text = "Login",
                fontSize = 12.sp)
        }

    }

}

@Preview
@Composable
fun SignupScreenPreview(){

}