import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.calendapp.R

package com.example.calendapp


class ProfileEditActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val profileImage: ImageView = findViewById(R.id.profile_image)

        // Asigna una imagen temporal desde los recursos
        profileImage.setImageResource(R.drawable.profile_placeholder) // Usa un nombre válido
    }
}
