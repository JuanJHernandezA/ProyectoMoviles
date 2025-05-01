package com.example.calendapp.registerUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.config.FirebaseConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddUserViewModel : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun onFieldChanged(field: String, value: String) {
        _userState.value = _userState.value.copy(
            user = _userState.value.user.copy(
                when (field) {
                    "cedula" -> cedula = value
                    "nombre" -> nombre = value
                    "apellido" -> apellido = value
                    "genero" -> genero = value
                    "edad" -> edad = value
                    "telefono" -> telefono = value
                    "rol" -> rol = value
                    "correo" -> correo = value
                    "contrasena" -> contrasena = value
                    else -> {}
                }
            )
        )
    }

    fun createUser() {
        viewModelScope.launch {
            try {
                _userState.value = _userState.value.copy(
                    isLoading = true,
                    error = null
                )

                val user = _userState.value.user

                // Validaciones básicas
                if (user.cedula.isEmpty() || user.nombre.isEmpty() || user.apellido.isEmpty() ||
                    user.genero.isEmpty() || user.edad.isEmpty() || user.telefono.isEmpty() ||
                    user.rol.isEmpty() || user.correo.isEmpty() || user.contrasena.isEmpty()) {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        error = "Todos los campos son obligatorios"
                    )
                    return@launch
                }

                // Crear usuario en Firebase Auth
                val authResult = FirebaseConfig.auth.createUserWithEmailAndPassword(
                    user.correo,
                    user.contrasena
                ).await()

                val uid = authResult.user?.uid
                if (uid != null) {
                    // Guardar información adicional en Firestore
                    val userData = hashMapOf(
                        "cedula" to user.cedula,
                        "nombre" to user.nombre,
                        "apellido" to user.apellido,
                        "genero" to user.genero,
                        "edad" to user.edad,
                        "telefono" to user.telefono,
                        "rol" to user.rol,
                        "correo" to user.correo
                    )

                    FirebaseConfig.firestore
                        .collection(FirebaseConfig.USERS_COLLECTION)
                        .document(uid)
                        .set(userData)
                        .await()
                
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                } else {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        error = "Error al crear el usuario"
                    )
                }
            } catch (e: Exception) {
                _userState.value = _userState.value.copy(
                    isLoading = false,
                    error = when (e) {
                        is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> "La contraseña es muy débil"
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "El correo electrónico no es válido"
                        is com.google.firebase.auth.FirebaseAuthUserCollisionException -> "El correo electrónico ya está en uso"
                        else -> "Error: ${e.message}"
                    }
                )
            }
        }
    }

    fun resetState() {
        _userState.value = UserState()
    }
} 