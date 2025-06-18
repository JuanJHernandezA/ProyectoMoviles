package com.example.calendapp.password_recovery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

data class PasswordRecoveryState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false
)

class PasswordRecoveryViewModel : ViewModel() {
    private val _state = MutableStateFlow(PasswordRecoveryState())
    val state: StateFlow<PasswordRecoveryState> = _state.asStateFlow()

    private var recoveryModel = PasswordRecoveryModel()
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    fun onEmailChanged(email: String) {
        _state.value = _state.value.copy(email = email)
        recoveryModel = recoveryModel.copy(email = email)
    }

    fun resetState() {
        _state.value = PasswordRecoveryState()
        recoveryModel = PasswordRecoveryModel()
    }

    fun recoverPassword() {
        if (!recoveryModel.isValidEmail()) {
            _state.value = _state.value.copy(
                error = "Por favor, ingrese un correo electrónico válido"
            )
            return
        }

        _state.value = _state.value.copy(
            isLoading = true,
            error = null
        )

        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(recoveryModel.email).await()
                _state.value = _state.value.copy(
                    isLoading = false,
                    isSuccess = true
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = "Correo inválido o no registrado en el sistema"
                )
            }
        }
    }

}
