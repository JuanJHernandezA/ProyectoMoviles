package com.example.calendapp.deleteUser.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calendapp.deleteUser.model.DeleteUserModel
import com.example.calendapp.deleteUser.model.DeleteUserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DeleteUserViewModel : ViewModel() {
    private val model = DeleteUserModel()
    
    private val _state = MutableStateFlow(DeleteUserState())
    val state: StateFlow<DeleteUserState> = _state.asStateFlow()

    fun updateEmail(email: String) {
        _state.update { it.copy(email = email) }
    }

    fun deleteUser() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null, success = false) }
            
            model.deleteUserByEmail(_state.value.email)
                .onSuccess {
                    _state.update { it.copy(
                        isLoading = false,
                        success = true,
                        error = null
                    )}
                }
                .onFailure { error ->
                    _state.update { it.copy(
                        isLoading = false,
                        success = false,
                        error = error.message ?: "Error desconocido"
                    )}
                }
        }
    }

    fun resetState() {
        _state.update { DeleteUserState() }
    }
} 