package com.example.calendapp.config

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserState(
    val userName: String? = null,
    val userRole: String? = null,
    val cedula: String? = null
)

class UserViewModel : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState.asStateFlow()

    fun updateUser(name: String?, role: String?, cedula: String?) {
        _userState.value = UserState(name, role, cedula)
    }

    fun clearUser() {
        _userState.value = UserState()
    }
} 