package com.example.calendapp.editar_usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.config.FirebaseConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditUserViewModel : ViewModel() {
    private val _userState = MutableStateFlow(EditUserState())
    val userState: StateFlow<EditUserState> = _userState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            try {
                _userState.value = _userState.value.copy(isLoading = true)
                
                val currentUser = FirebaseConfig.auth.currentUser
                if (currentUser == null) {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        error = "No hay usuario autenticado"
                    )
                    return@launch
                }

                // Obtener datos del usuario desde Firestore
                val userDoc = FirebaseConfig.firestore
                    .collection(FirebaseConfig.USERS_COLLECTION)
                    .document(currentUser.uid)
                    .get()
                    .await()

                _userState.value = _userState.value.copy(
                    user = EditUserModel(
                        email = currentUser.email ?: "",
                        nombre = userDoc.getString("nombre") ?: "",
                        apellido = userDoc.getString("apellido") ?: "",
                        telefono = userDoc.getString("telefono") ?: "",
                        genero = userDoc.getString("genero") ?: "",
                        rol = userDoc.getString("rol") ?: ""
                    ),
                    isLoading = false
                )
            } catch (e: Exception) {
                _userState.value = _userState.value.copy(
                    isLoading = false,
                    error = "Error al cargar datos del usuario: ${e.message}"
                )
            }
        }
    }

    fun onFieldChanged(field: String, value: String) {
        _userState.value = _userState.value.copy(
            user = _userState.value.user.copy(
                email = if (field == "email") value else _userState.value.user.email,
                password = if (field == "password") value else _userState.value.user.password,
                confirmPassword = if (field == "confirmPassword") value else _userState.value.user.confirmPassword,
                nombre = if (field == "nombre") value else _userState.value.user.nombre,
                apellido = if (field == "apellido") value else _userState.value.user.apellido,
                telefono = if (field == "telefono") value else _userState.value.user.telefono,
                genero = if (field == "genero") value else _userState.value.user.genero,
                rol = if (field == "rol") value else _userState.value.user.rol
            )
        )
    }

    fun updateUser() {
        viewModelScope.launch {
            try {
                _userState.value = _userState.value.copy(
                    isLoading = true,
                    error = null
                )

                val currentUser = FirebaseConfig.auth.currentUser
                if (currentUser == null) {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        error = "No hay usuario autenticado"
                    )
                    return@launch
                }

                val user = _userState.value.user

                // Validaciones básicas
                if (user.nombre.isEmpty() || user.apellido.isEmpty() ||
                    user.telefono.isEmpty() || user.genero.isEmpty()) {
                    _userState.value = _userState.value.copy(
                        isLoading = false,
                        error = "Todos los campos son obligatorios"
                    )
                    return@launch
                }

                // Actualizar contraseña si se proporcionó una nueva
                if (user.password.isNotEmpty()) {
                    if (user.password != user.confirmPassword) {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            error = "Las contraseñas no coinciden"
                        )
                        return@launch
                    }
                    if (user.password.length < 6) {
                        _userState.value = _userState.value.copy(
                            isLoading = false,
                            error = "La contraseña debe tener al menos 6 caracteres"
                        )
                        return@launch
                    }
                    currentUser.updatePassword(user.password).await()
                }

                // Actualizar información en Firestore
                val userData = hashMapOf(
                    "nombre" to user.nombre,
                    "apellido" to user.apellido,
                    "telefono" to user.telefono,
                    "genero" to user.genero,
                    "email" to user.email,
                    "rol" to user.rol
                ) as Map<String, Any>

                // Primero intentamos actualizar el documento
                try {
                    FirebaseConfig.firestore
                        .collection(FirebaseConfig.USERS_COLLECTION)
                        .document(currentUser.uid)
                        .update(userData)
                        .await()
                } catch (e: Exception) {
                    // Si el documento no existe, lo creamos
                    FirebaseConfig.firestore
                        .collection(FirebaseConfig.USERS_COLLECTION)
                        .document(currentUser.uid)
                        .set(userData)
                        .await()
                }

                _userState.value = _userState.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _userState.value = _userState.value.copy(
                    isLoading = false,
                    error = when (e) {
                        is com.google.firebase.auth.FirebaseAuthWeakPasswordException -> "La contraseña es muy débil"
                        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException -> "Credenciales inválidas"
                        is com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException -> "Por favor, vuelve a iniciar sesión para cambiar tu contraseña"
                        else -> "Error: ${e.message}"
                    }
                )
            }
        }
    }

    fun resetState() {
        _userState.value = EditUserState()
    }
} 