package com.example.calendapp.utils

import android.util.Log
import java.util.Properties
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class EmailService {
    private val TAG = "EmailService"
    private val username = "calendapp.notifications@gmail.com"
    private val password = "bljw gnnq alvy vxnj" 

    fun sendEmail(to: String, subject: String, body: String) {
        Thread {
            try {
                val props = Properties().apply {
                    put("mail.smtp.auth", "true")
                    put("mail.smtp.starttls.enable", "true")
                    put("mail.smtp.host", "smtp.gmail.com")
                    put("mail.smtp.port", "587")
                }

                val session = Session.getInstance(props, object : Authenticator() {
                    override fun getPasswordAuthentication(): PasswordAuthentication {
                        return PasswordAuthentication(username, password)
                    }
                })

                val message = MimeMessage(session).apply {
                    setFrom(InternetAddress(username))
                    setRecipients(Message.RecipientType.TO, InternetAddress.parse(to))
                    setSubject(subject)
                    setText(body)
                }

                Transport.send(message)
                Log.d(TAG, "Correo enviado exitosamente a $to")
            } catch (e: Exception) {
                Log.e(TAG, "Error al enviar correo: ${e.message}")
                e.printStackTrace()
            }
        }.start()
    }
} 