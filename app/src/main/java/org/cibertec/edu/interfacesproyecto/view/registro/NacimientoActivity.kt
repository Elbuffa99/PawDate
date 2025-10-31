package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import java.text.SimpleDateFormat
import java.util.*

class NacimientoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nacimiento)

        val txtNacimiento = findViewById<EditText>(R.id.NomPerro)
        val btnSiguiente = findViewById<Button>(R.id.BSiguiente)
        val session = SessionManager(this)

        btnSiguiente.setOnClickListener {
            val texto = txtNacimiento.text.toString().trim()

            if (texto.isEmpty()) {
                Toast.makeText(this, "Ingresa la fecha de nacimiento", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                // Intentamos convertir el texto a fecha válida (ejemplo: 12/03/2021)
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fecha = formato.parse(texto)
                val fechaMillis = fecha?.time ?: throw Exception("Formato inválido")

                // Guardamos en SessionManager como milisegundos
                session.guardarDatoTemporal("fecha_nacimiento", fechaMillis.toString())

                Toast.makeText(this, "Fecha guardada correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, GeneroActivity::class.java))
                finish()

            } catch (e: Exception) {
                Toast.makeText(this, "Formato inválido. Usa dd/MM/yyyy", Toast.LENGTH_LONG).show()
            }
        }
    }
}
