package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager

class GeneroActivity : AppCompatActivity() {

    private var generoSeleccionado: String? = null  // Guarda temporalmente la selección

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_genero)

        val btnMacho = findViewById<Button>(R.id.BMacho)
        val btnHembra = findViewById<Button>(R.id.BHembra)
        val btnSiguiente = findViewById<Button>(R.id.BSiguiente)
        val session = SessionManager(this)

        // 🟠 Acción al pulsar "Macho"
        btnMacho.setOnClickListener {
            generoSeleccionado = "Macho"
            Toast.makeText(this, "Seleccionaste: Macho 🐶", Toast.LENGTH_SHORT).show()
        }

        // 🟣 Acción al pulsar "Hembra"
        btnHembra.setOnClickListener {
            generoSeleccionado = "Hembra"
            Toast.makeText(this, "Seleccionaste: Hembra 🐕‍🦺", Toast.LENGTH_SHORT).show()
        }

        // ✅ Acción al pulsar "Siguiente"
        btnSiguiente.setOnClickListener {
            if (generoSeleccionado == null) {
                Toast.makeText(this, "Selecciona un género para continuar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Guardamos en SessionManager
            session.guardarDatoTemporal("genero", generoSeleccionado!!)

            Toast.makeText(this, "Género guardado correctamente", Toast.LENGTH_SHORT).show()

            // Pasamos a RelacionesActivity
            startActivity(Intent(this, RelacionesActivity::class.java))
            finish()
        }
    }
}
