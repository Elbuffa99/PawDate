package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager

class NombreActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nombre)

        val txtNombrePerro = findViewById<EditText>(R.id.NomPerro)
        val btnSiguiente = findViewById<Button>(R.id.BSiguiente)
        val session = SessionManager(this)

        btnSiguiente.setOnClickListener {
            val nombre = txtNombrePerro.text.toString()

            if (nombre.isEmpty()) {
                Toast.makeText(this, "Ingresa el nombre de tu perrito", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            session.guardarDatoTemporal("nombre_perro", nombre)
            startActivity(Intent(this, NacimientoActivity::class.java))
        }
    }
}
