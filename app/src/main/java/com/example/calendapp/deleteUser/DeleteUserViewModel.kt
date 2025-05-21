package com.example.calendapp.deleteUser

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.functions.FirebaseFunctions

class DeleteUserViewModel : ViewModel() {
    private val db = Firebase.firestore
    private val functions = FirebaseFunctions.getInstance()

    fun deleteUserByCorreo(correo: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        // Buscar el documento por el campo "correo"
        db.collection("users")
            .whereEqualTo("correo", correo)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (querySnapshot.isEmpty) {
                    onFailure("No se encontró ningún usuario con el correo: $correo")
                } else {
                    // Eliminar todos los documentos que coincidan
                    val batch = db.batch()
                    for (document in querySnapshot.documents) {
                        batch.delete(document.reference)
                    }

                    batch.commit()
                        .addOnSuccessListener {
                            println("Usuario eliminado de Firestore")

                        }
                        .addOnFailureListener { e ->
                            onFailure("Error al eliminar de Firestore: ${e.message}")
                        }
                }
            }
            .addOnFailureListener { e ->
                onFailure("Error al buscar usuario: ${e.message}")
            }
    }
}
