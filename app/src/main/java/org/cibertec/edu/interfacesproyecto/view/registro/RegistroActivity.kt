package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.view.login.LoginActivity // Asegúrate de importar LoginActivity

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val txtEmail = findViewById<EditText>(R.id.Email)
        val txtTelefono = findViewById<EditText>(R.id.Telefono)
        val btnSiguiente = findViewById<Button>(R.id.BSiguiente)
        val flechaBack = findViewById<ImageView>(R.id.Flecha)  // Aquí capturamos la flecha

        val session = SessionManager(this)

        // Flecha de regreso a LoginActivity
        flechaBack.setOnClickListener {
            // Redirige a LoginActivity cuando se hace clic en la flecha
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()  // Opcional, para asegurarte de que se cierre esta actividad
        }

        btnSiguiente.setOnClickListener {
            val email = txtEmail.text.toString()
            val telefono = txtTelefono.text.toString()

            if (email.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            session.guardarDatoTemporal("email", email)
            session.guardarDatoTemporal("telefono", telefono)

            startActivity(Intent(this, NombreActivity::class.java))
        }
    }
}
