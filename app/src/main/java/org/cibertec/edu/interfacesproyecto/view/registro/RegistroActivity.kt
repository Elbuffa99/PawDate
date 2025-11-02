package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.view.login.LoginActivity

class RegistroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        val txtEmail = findViewById<EditText>(R.id.Email)
        val txtTelefono = findViewById<EditText>(R.id.Telefono)
        val btnSiguiente = findViewById<Button>(R.id.BSiguiente)
        val flechaBack = findViewById<ImageView>(R.id.Flecha)

        val session = SessionManager(this)

        // ✅ Limita el teléfono a 9 dígitos máximo
        txtTelefono.filters = arrayOf(InputFilter.LengthFilter(9))

        // Flecha para regresar a LoginActivity
        flechaBack.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btnSiguiente.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val telefono = txtTelefono.text.toString().trim()

            // Validar campos vacíos
            if (email.isEmpty() || telefono.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Validación de email con dominio específico (gmail o hotmail)
            val emailRegex = Regex("^[A-Za-z0-9._%+-]+@(gmail|hotmail)\\.com$", RegexOption.IGNORE_CASE)

            if (!emailRegex.matches(email)) {
                Toast.makeText(this, "Correo debe ser @gmail.com o @hotmail.com", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Validación del número telefónico exacto de 9 dígitos
            if (telefono.length != 9 || !telefono.all { it.isDigit() }) {
                Toast.makeText(this, "El número debe tener 9 dígitos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardar datos si todo es válido
            session.guardarDatoTemporal("email", email)
            session.guardarDatoTemporal("telefono", telefono)

            startActivity(Intent(this, NombreActivity::class.java))
        }
    }
}
