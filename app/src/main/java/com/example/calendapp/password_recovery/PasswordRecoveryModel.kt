package com.example.calendapp.password_recovery

data class PasswordRecoveryModel(
    val email: String = "",
    val timestamp: Long = System.currentTimeMillis()
)