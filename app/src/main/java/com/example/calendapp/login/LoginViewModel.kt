package com.example.calendapp.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.config.FirebaseConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LoginViewModel : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

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

                val authResult = FirebaseConfig.auth.signInWithEmailAndPassword(email, password).await()
                val uid = authResult.user?.uid

                if (uid != null) {
                    val document = FirebaseConfig.firestore
                        .collection(FirebaseConfig.USERS_COLLECTION)
                        .document(uid)
                        .get()
                        .await()

                    val storedEmail = document.getString("correo")
                    if (storedEmail != email) {
                        _loginState.value = _loginState.value.copy(
                            isLoading = false,
                            error = "Error de autenticación: correo no coincide"
                        )
                        return@launch
                    }

                    val role = document.getString("rol")
                    if (role == null) {
                        _loginState.value = _loginState.value.copy(
                            isLoading = false,
                            error = "Error: usuario sin rol asignado"
                        )
                        return@launch
                    }

                    if (role != "admin" && role != "usuario") {
                        _loginState.value = _loginState.value.copy(
                            isLoading = false,
                            error = "Error: rol de usuario no válido"
                        )
                        return@launch
                    }

                    _loginState.value = _loginState.value.copy(
                        isLoading = false,
                        isSuccess = true,
                        userRole = role
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
