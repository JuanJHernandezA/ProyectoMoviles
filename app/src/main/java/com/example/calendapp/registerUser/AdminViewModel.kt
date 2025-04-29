package com.example.calendapp.registerUser

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _registerState = MutableStateFlow<RegisterResult>(RegisterResult.Idle)
    val registerState: StateFlow<RegisterResult> = _registerState

    fun registerUser(email: String, password: String) {
        _registerState.value = RegisterResult.Loading

        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener { result ->
                    val uid = result.user?.uid ?: return@addOnSuccessListener

                    val user = hashMapOf(
                        "email" to email,
                        "role" to "user"
                    )

                    firestore.collection("users").document(uid)
                        .set(user)
                        .addOnSuccessListener {
                            _registerState.value = RegisterResult.Success("Usuario creado exitosamente")
                        }
                        .addOnFailureListener { e ->
                            _registerState.value = RegisterResult.Error("Error guardando usuario: ${e.message}")
                        }
                }
                .addOnFailureListener { e ->
                    _registerState.value = RegisterResult.Error("Error creando usuario: ${e.message}")
                }
        }
    }

    fun resetState() {
        _registerState.value = RegisterResult.Idle
    }

    sealed class RegisterResult {
        object Idle : RegisterResult()
        object Loading : RegisterResult()
        data class Success(val message: String) : RegisterResult()
        data class Error(val message: String) : RegisterResult()
    }
}
