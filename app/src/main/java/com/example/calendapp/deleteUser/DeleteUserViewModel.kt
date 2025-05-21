package com.example.calendapp.deleteUser

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.functions.FirebaseFunctions


class DeleteUserViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val functions = FirebaseFunctions.getInstance()

    fun deleteUserByEmail(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // 1. Eliminar de Firestore
        db.collection("users").document(email).delete()
            .addOnSuccessListener {
                // 2. Llamar Cloud Function con email
                functions
                    .getHttpsCallable("deleteUserByAdmin")
                    .call(hashMapOf("email" to email))
                    .addOnSuccessListener {
                        onSuccess()
                    }
                    .addOnFailureListener { e ->
                        onFailure("Error al eliminar de Auth: ${e.message}")
                    }
            }
            .addOnFailureListener {
                onFailure("Error al eliminar de Firestore: ${it.message}")
            }
    }
}
