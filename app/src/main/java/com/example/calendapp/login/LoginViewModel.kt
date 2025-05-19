package com.example.calendapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    fun onEmailChanged(newEmail: String) {
        _loginState.value = _loginState.value.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        _loginState.value = _loginState.value.copy(password = newPassword)
    }

    fun login() {
        viewModelScope.launch {
            try {
                _loginState.value = _loginState.value.copy(
                    isLoading = true,
                    error = null
                )

                val email = _loginState.value.email.trim()
                val password = _loginState.value.password.trim()

                if (email.isEmpty() || password.isEmpty()) {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = "Por favor ingresa email y contraseña"
                    )
                    return@launch
                }

                val result = auth.signInWithEmailAndPassword(email, password).await()
                val user = result.user
                
                if (user != null) {
                    val userDoc = db.collection("users").document(user.uid).get().await()
                    val nombre = userDoc.getString("nombre")
                    val rol = userDoc.getString("rol")
                    val cedula = userDoc.getString("cedula")
                    
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        userName = nombre,
                        userRole = rol,
                        cedula = cedula
                    )
                } else {
                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        error = "Error al obtener información del usuario"
                    )
                }
            } catch (e: Exception) {
                _loginState.value = _loginState.value.copy(
                    isLoading = false,
                    error = when (e) {
                        is com.google.firebase.auth.FirebaseAuthInvalidUserException -> "Usuario no encontrado"
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas"
                        else -> "Error: ${e.message}"
                    }
                )
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState()
    }
}
