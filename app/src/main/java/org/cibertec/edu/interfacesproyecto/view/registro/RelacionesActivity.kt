package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.model.dao.PerfilDAO

class RelacionesActivity : AppCompatActivity() {

    private lateinit var perfilDAO: PerfilDAO
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_relaciones)

        perfilDAO = PerfilDAO(this)
        session = SessionManager(this)

        val bUno = findViewById<Button>(R.id.BUno)
        val bDos = findViewById<Button>(R.id.BDos)
        val bTres = findViewById<Button>(R.id.BTres)
        val bCuatro = findViewById<Button>(R.id.BCuatro)
        val bCinco = findViewById<Button>(R.id.BCinco)
        val bSeis = findViewById<Button>(R.id.BSeis)

        // Función genérica
        fun guardarYSeguir(busca: String) {
            AlertDialog.Builder(this)
                .setTitle("Confirmar elección")
                .setMessage("¿Deseas guardar \"$busca\" como la relación de tu perrito?")
                .setPositiveButton("Sí") { _, _ -> insertarPerfil(busca) }
                .setNegativeButton("No", null)
                .show()
        }

        // Asignar listeners
        bUno.setOnClickListener { guardarYSeguir("Amistad peluda") }
        bDos.setOnClickListener { guardarYSeguir("Amor perruno") }
        bTres.setOnClickListener { guardarYSeguir("Compañero de juegos") }
        bCuatro.setOnClickListener { guardarYSeguir("Pasear juntos") }
        bCinco.setOnClickListener { guardarYSeguir("Criar cachorros juntos") }
        bSeis.setOnClickListener { guardarYSeguir("Rival amistoso") }
    }

    // 🐾 Inserta el perfil en la BD
    private fun insertarPerfil(busca: String) {
        val email = session.obtenerDatoTemporal("email")
        val telefono = session.obtenerDatoTemporal("telefono")
        val nombre = session.obtenerDatoTemporal("nombre_perro")
        val fechaStr = session.obtenerDatoTemporal("fecha_nacimiento")
        val genero = session.obtenerDatoTemporal("genero")

        Log.d("RelacionesActivity", "📦 Datos recuperados → email=$email, tel=$telefono, nombre=$nombre, fecha=$fechaStr, genero=$genero, busca=$busca")

        if (email == null || telefono == null || nombre == null || fechaStr == null || genero == null) {
            Toast.makeText(this, "Faltan datos para crear el perfil", Toast.LENGTH_LONG).show()
            Log.e("RelacionesActivity", "❌ Faltan datos en SessionManager, no se puede crear el perfil.")
            return
        }

        val fecha = fechaStr.toLongOrNull() ?: 0L
        val ok = perfilDAO.insertarPerfil(
            email = email,
            telefono = telefono,
            nombrePerro = nombre,
            fechaNacimiento = fecha,
            genero = genero,
            busca = busca
        )

        if (ok) {
            val idPerfil = session.obtenerIdPerfil()
            Log.d("RelacionesActivity", "✅ Perfil insertado correctamente (ID=$idPerfil), redirigiendo a HabitosActivity")
            Toast.makeText(this, "Perfil creado correctamente 🎉", Toast.LENGTH_SHORT).show()

            // 🚀 Ir a HabitosActivity
            val intent = Intent(this, HabitosActivity::class.java)
            intent.putExtra("id_perfil", idPerfil)
            startActivity(intent)
            finish()

        } else {
            Toast.makeText(this, "Error al crear el perfil 😔", Toast.LENGTH_SHORT).show()
            Log.e("RelacionesActivity", "❌ Error al insertar perfil en la base de datos.")
        }
    }
}
