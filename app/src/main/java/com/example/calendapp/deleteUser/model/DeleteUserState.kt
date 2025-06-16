package com.example.calendapp.deleteUser.model

data class DeleteUserState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
) 