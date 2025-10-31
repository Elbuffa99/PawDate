package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.PersonalidadDAO

class PersonalidadActivity : AppCompatActivity() {

    private lateinit var personalidadDAO: PersonalidadDAO

    private var comportamientoSeleccionado = ""
    private var entornoSeleccionado = ""
    private var interaccionSeleccionada = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personalidad)

        personalidadDAO = PersonalidadDAO(this)

        // === 1️⃣ Referencias a chips de ejemplo ===
        val chipsComportamiento = listOf(
            findViewById<TextView>(R.id.CManso),
            findViewById<TextView>(R.id.CCarinhoso),
            findViewById<TextView>(R.id.CAfectuoso),
            findViewById<TextView>(R.id.CIndepen),
            findViewById<TextView>(R.id.CSolitario),
            findViewById<TextView>(R.id.CProtector),
            findViewById<TextView>(R.id.CTimido),
            findViewById<TextView>(R.id.CTerritorial)
        )

        val chipsEntorno = listOf(
            findViewById<TextView>(R.id.CCurioso),
            findViewById<TextView>(R.id.CExplorador),
            findViewById<TextView>(R.id.CTranquilo),
            findViewById<TextView>(R.id.CDestructor),
            findViewById<TextView>(R.id.CObediente),
            findViewById<TextView>(R.id.CLadrador),
            findViewById<TextView>(R.id.CTravieso),
            findViewById<TextView>(R.id.CTestarudo)
        )

        val chipsInteraccion = listOf(
            findViewById<TextView>(R.id.ChipBP),
            findViewById<TextView>(R.id.CBN),
            findViewById<TextView>(R.id.ChipCeloso),
            findViewById<TextView>(R.id.CPG),
            findViewById<TextView>(R.id.CCE)
        )

        val botonSiguiente = findViewById<Button>(R.id.BSiguiente)

        // === 2️⃣ Cambiar color al seleccionar chip ===
        fun manejarSeleccion(chip: TextView, grupo: List<TextView>, tipo: String) {
            grupo.forEach { it.setBackgroundResource(R.drawable.chip_bg_gray) }
            chip.setBackgroundResource(R.drawable.chip_bg_selected)

            when (tipo) {
                "comportamiento" -> comportamientoSeleccionado = chip.text.toString()
                "entorno" -> entornoSeleccionado = chip.text.toString()
                "interaccion" -> interaccionSeleccionada = chip.text.toString()
            }
        }

        chipsComportamiento.forEach { chip ->
            chip.setOnClickListener { manejarSeleccion(chip, chipsComportamiento, "comportamiento") }
        }

        chipsEntorno.forEach { chip ->
            chip.setOnClickListener { manejarSeleccion(chip, chipsEntorno, "entorno") }
        }

        chipsInteraccion.forEach { chip ->
            chip.setOnClickListener { manejarSeleccion(chip, chipsInteraccion, "interaccion") }
        }

        // === 3️⃣ Botón siguiente guarda y pasa a Avatar ===
        botonSiguiente.setOnClickListener {
            if (comportamientoSeleccionado.isEmpty() ||
                entornoSeleccionado.isEmpty() ||
                interaccionSeleccionada.isEmpty()
            ) {
                Toast.makeText(this, "Selecciona una opción en cada categoría", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = personalidadDAO.insertarPersonalidad(
                comportamientoSeleccionado,
                entornoSeleccionado,
                interaccionSeleccionada
            )

            if (exito) {
                Toast.makeText(this, "✅ Personalidad guardada correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AvatarsActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "❌ Error al guardar la personalidad", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
