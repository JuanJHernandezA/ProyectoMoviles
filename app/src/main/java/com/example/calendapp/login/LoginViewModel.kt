package com.example.calendapp.login

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    var credentials = mutableStateOf(Model())
        private set

    var loginSuccess = mutableStateOf(false)
        private set

    var isAdmin = mutableStateOf(false)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun onEmailChanged(newEmail: String) {
        credentials.value = credentials.value.copy(email = newEmail)
    }

    fun onPasswordChanged(newPassword: String) {
        credentials.value = credentials.value.copy(password = newPassword)
    }

    fun login() {
        errorMessage.value = null

        auth.signInWithEmailAndPassword(
            credentials.value.email.trim(),
            credentials.value.password.trim()
        ).addOnSuccessListener { result ->
            val uid = result.user?.uid
            if (uid != null) {
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        val role = document.getString("role")
                        isAdmin.value = role == "admin"
                        loginSuccess.value = true
                    }
                    .addOnFailureListener {
                        errorMessage.value = "No se pudo verificar el rol del usuario."
                    }
            } else {
                errorMessage.value = "No se encontró el UID del usuario."
            }
        }.addOnFailureListener { exception ->
            errorMessage.value = "Error de autenticación: ${exception.message}"
        }
    }

    fun resetLoginState() {
        loginSuccess.value = false
        isAdmin.value = false
    }
}
