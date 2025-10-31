package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.model.dao.HabitosDAO

class HabitosActivity : AppCompatActivity() {

    private lateinit var habitosDAO: HabitosDAO
    private lateinit var session: SessionManager

    private var nivelEnergia: String? = null
    private var frecuenciaPaseos: String? = null
    private var sociabilidad: String? = null
    private var alimentacion: String = "Balanceada" // valor por defecto o se pedirá después
    private var horariosDescanso: String = "Regular" // valor por defecto o se pedirá después

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habitos)

        habitosDAO = HabitosDAO(this)
        session = SessionManager(this)

        val idPerfil = session.obtenerIdPerfil()
        if (idPerfil == -1) {
            Toast.makeText(this, "⚠️ No se encontró perfil en sesión", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        // ============================
        // 🔹 Niveles de Energía
        // ============================
        val chipEnergia1 = findViewById<TextView>(R.id.Chip1)
        val chipEnergia2 = findViewById<TextView>(R.id.Chip2)
        val chipEnergia3 = findViewById<TextView>(R.id.Chip3)

        chipEnergia1.setOnClickListener { seleccionarOpcionEnergia(chipEnergia1, "Muy activo") }
        chipEnergia2.setOnClickListener { seleccionarOpcionEnergia(chipEnergia2, "Tranquilo") }
        chipEnergia3.setOnClickListener { seleccionarOpcionEnergia(chipEnergia3, "Intermedio") }

        // ============================
        // 🔹 Frecuencia de Paseos
        // ============================
        val lyt2 = findViewById<android.widget.LinearLayout>(R.id.LLyt2)
        val chipPaseo1 = lyt2.getChildAt(0) as TextView
        val chipPaseo2 = lyt2.getChildAt(1) as TextView
        val chipPaseo3 = lyt2.getChildAt(2) as TextView

        chipPaseo1.setOnClickListener { seleccionarOpcionPaseos(chipPaseo1, "1 vez al día") }
        chipPaseo2.setOnClickListener { seleccionarOpcionPaseos(chipPaseo2, "2 a 3 veces al día") }
        chipPaseo3.setOnClickListener { seleccionarOpcionPaseos(chipPaseo3, "Más de 3 veces al día") }

        // ============================
        // 🔹 Sociabilidad
        // ============================
        val lyt3 = findViewById<android.widget.LinearLayout>(R.id.LLyt3)
        val chipSoc1 = lyt3.getChildAt(0) as TextView
        val chipSoc2 = lyt3.getChildAt(1) as TextView
        val chipSoc3 = lyt3.getChildAt(2) as TextView

        chipSoc1.setOnClickListener { seleccionarOpcionSociabilidad(chipSoc1, "Le encanta conocer otros perros") }
        chipSoc2.setOnClickListener { seleccionarOpcionSociabilidad(chipSoc2, "Prefiere pocas interacciones") }
        chipSoc3.setOnClickListener { seleccionarOpcionSociabilidad(chipSoc3, "Es tímido con extraños") }

        // ============================
        // 🔹 Botón siguiente
        // ============================
        val btnSiguiente = findViewById<Button>(R.id.BSiguiente)
        btnSiguiente.setOnClickListener {
            if (nivelEnergia == null || frecuenciaPaseos == null || sociabilidad == null) {
                Toast.makeText(this, "Selecciona todas las opciones 🐾", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val exito = habitosDAO.insertarHabitos(
                nivelEnergia!!,
                frecuenciaPaseos!!,
                sociabilidad!!,
                alimentacion,
                horariosDescanso
            )

            if (exito) {
                Toast.makeText(this, "✅ Hábitos guardados correctamente", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, PersonalidadActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "❌ Error al guardar los hábitos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // ============================
    // 🟢 Métodos auxiliares para marcar selección
    // ============================

    private fun seleccionarOpcionEnergia(chip: TextView, valor: String) {
        limpiarSeleccionEnergia()
        chip.setBackgroundResource(R.drawable.chip_bg_selected)
        nivelEnergia = valor
    }

    private fun limpiarSeleccionEnergia() {
        findViewById<TextView>(R.id.Chip1).setBackgroundResource(R.drawable.chip_bg_gray)
        findViewById<TextView>(R.id.Chip2).setBackgroundResource(R.drawable.chip_bg_gray)
        findViewById<TextView>(R.id.Chip3).setBackgroundResource(R.drawable.chip_bg_gray)
    }

    private fun seleccionarOpcionPaseos(chip: TextView, valor: String) {
        limpiarSeleccionPaseos()
        chip.setBackgroundResource(R.drawable.chip_bg_selected)
        frecuenciaPaseos = valor
    }

    private fun limpiarSeleccionPaseos() {
        val lyt = findViewById<android.widget.LinearLayout>(R.id.LLyt2)
        for (i in 0 until lyt.childCount) {
            (lyt.getChildAt(i) as TextView).setBackgroundResource(R.drawable.chip_bg_gray)
        }
    }

    private fun seleccionarOpcionSociabilidad(chip: TextView, valor: String) {
        limpiarSeleccionSociabilidad()
        chip.setBackgroundResource(R.drawable.chip_bg_selected)
        sociabilidad = valor
    }

    private fun limpiarSeleccionSociabilidad() {
        val lyt = findViewById<android.widget.LinearLayout>(R.id.LLyt3)
        for (i in 0 until lyt.childCount) {
            (lyt.getChildAt(i) as TextView).setBackgroundResource(R.drawable.chip_bg_gray)
        }
    }
}
