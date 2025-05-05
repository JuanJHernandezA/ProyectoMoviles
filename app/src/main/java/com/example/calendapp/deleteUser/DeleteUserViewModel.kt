package com.example.calendapp.deleteUser

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class DeleteUserViewModel : ViewModel() {
    fun deleteUserByEmail(email: String, onSuccess: () -> Unit, onFailure: () -> Unit) {
        val db = Firebase.firestore
        db.collection("users").document(email).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure() }
    }
}
