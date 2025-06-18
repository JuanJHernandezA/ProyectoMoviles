package com.example.calendapp.password_recovery

data class PasswordRecoveryModel(
    val email: String = "",
    val timestamp: Long = System.currentTimeMillis()
) {
    fun isValidEmail(): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun toMap(): Map<String, Any> {
        return mapOf(
            "email" to email,
            "timestamp" to timestamp
        )
    }
} 