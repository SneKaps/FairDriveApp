package com.example.fairdriveapp

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> = _authState

    init {
        checkAuthState()
    }

    fun checkAuthState(){
        if(auth.currentUser == null){
            _authState.value = AuthState.unauthenticated
        }
        else{
            _authState.value = AuthState.authenticated
        }
    }

    fun login(email:String, pwd:String){

        if(email.isEmpty()||pwd.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value =  AuthState.loading
        auth.signInWithEmailAndPassword(email, pwd)
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    _authState.value = AuthState.authenticated
                }
                else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signup(email:String, pwd:String){

        if(email.isEmpty()||pwd.isEmpty()){
            _authState.value = AuthState.Error("Email or password can't be empty")
            return
        }
        _authState.value =  AuthState.loading
        auth.createUserWithEmailAndPassword(email, pwd)
            .addOnCompleteListener {task->
                if (task.isSuccessful){
                    _authState.value = AuthState.authenticated
                }
                else{
                    _authState.value = AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signOut(){
        auth.signOut()
        _authState.value = AuthState.unauthenticated
    }

}
sealed class AuthState{
    object authenticated: AuthState()
    object unauthenticated: AuthState()
    object loading: AuthState()
    data class Error(val message: String): AuthState()
}