package org.cibertec.edu.interfacesproyecto.view.registro

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
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
        val flechaBack = findViewById<ImageView>(R.id.Flecha)

        // ✅ Evitar teclado
        txtNacimiento.isFocusable = false
        txtNacimiento.isClickable = true

        // ✅ Mostrar calendario al hacer clic
        txtNacimiento.setOnClickListener {
            val calendario = Calendar.getInstance()
            val year = calendario.get(Calendar.YEAR)
            val month = calendario.get(Calendar.MONTH)
            val day = calendario.get(Calendar.DAY_OF_MONTH)

            val datePicker = DatePickerDialog(
                this,
                { _, y, m, d ->
                    val fechaSeleccionada = String.format("%02d/%02d/%04d", d, m + 1, y)
                    txtNacimiento.setText(fechaSeleccionada)
                },
                year, month, day
            )
            datePicker.show()
        }

        flechaBack.setOnClickListener {
            startActivity(Intent(this, NombreActivity::class.java))
            finish()
        }

        btnSiguiente.setOnClickListener {
            val texto = txtNacimiento.text.toString().trim()

            if (texto.isEmpty()) {
                Toast.makeText(this, "Selecciona una fecha", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            try {
                val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val fecha = formato.parse(texto)
                session.guardarDatoTemporal("fecha_nacimiento", fecha!!.time.toString())

                startActivity(Intent(this, GeneroActivity::class.java))
                finish()

            } catch (e: Exception) {
                Toast.makeText(this, "Formato inválido", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
