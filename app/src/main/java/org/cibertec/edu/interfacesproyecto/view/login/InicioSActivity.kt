package org.cibertec.edu.interfacesproyecto.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.view.menu.MenuActivity // o donde vaya después

class InicioSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sactivity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Referencias a los campos
        val txtEmail = findViewById<TextInputEditText>(R.id.Email)
        val txtNombrePerro = findViewById<TextInputEditText>(R.id.Contrasenha)
        val btnIngresar = findViewById<Button>(R.id.boton)

        btnIngresar.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val nombrePerro = txtNombrePerro.text.toString().trim()

            // Validar que no estén vacíos
            if (email.isEmpty() || nombrePerro.isEmpty()) {
                Toast.makeText(this, "Por favor completa ambos campos 🐾", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔒 Validación temporal: el campo “Contraseña” será nombre_perro
            // (Más adelante lo validas contra una base de datos o archivo)
            if (nombrePerro.lowercase() == "rocky" || nombrePerro.lowercase() == "luna") {
                Toast.makeText(this, "Bienvenido a PawDate, $nombrePerro 🐶", Toast.LENGTH_SHORT).show()

                // Ejemplo: ir al menú principal
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this, "Nombre de perro no registrado 🐕", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
