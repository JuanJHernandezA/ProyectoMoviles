package com.example.calendapp.deleteUser.model

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class DeleteUserModel {
    private val db = FirebaseFirestore.getInstance()

    suspend fun deleteUserByEmail(email: String): Result<Unit> = runCatching {
        val querySnapshot = db.collection("users")
            .whereEqualTo("correo", email)
            .get()
            .await()

        if (querySnapshot.isEmpty) {
            throw Exception("No se encontró ningún usuario con el correo: $email")
        }

        val batch = db.batch()
        
        // Obtener la cédula del usuario antes de eliminarlo
        val cedula = querySnapshot.documents.firstOrNull()?.getString("cedula")
        
        if (cedula != null) {
            // Eliminar horarios relacionados
            val horariosSnapshot = db.collection("horarios")
                .whereEqualTo("empleadoId", cedula)
                .get()
                .await()
            
            horariosSnapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }

            // Eliminar notificaciones relacionadas
            val notificacionesSnapshot = db.collection("notificaciones")
                .whereEqualTo("cedulaEmpleado", cedula)
                .get()
                .await()
            
            notificacionesSnapshot.documents.forEach { document ->
                batch.delete(document.reference)
            }
        }

        // Eliminar el usuario
        querySnapshot.documents.forEach { document ->
            batch.delete(document.reference)
        }

        batch.commit().await()
    }
} 