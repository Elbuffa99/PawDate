package org.cibertec.edu.interfacesproyecto.view.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.hdodenhof.circleimageview.CircleImageView
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.LoginDAO
import org.cibertec.edu.interfacesproyecto.model.dao.MatchDAO

class MatchEncuentroActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_match_encuentro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val BtnMenu = findViewById<ImageView>(R.id.btnBack)
        BtnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // opcional: cierra la actividad actual
        }

        // Obtener perfil logueado
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val idPerfilLogueado = prefs.getInt("id_perfil", -1)

        if (idPerfilLogueado == -1) {
            Toast.makeText(this, "⚠️ No hay sesión activa.", Toast.LENGTH_SHORT).show()
            return
        }

        val matchDAO = MatchDAO(this)
        val loginDAO = LoginDAO(this)
        val matchesMutuos = matchDAO.obtenerMatchesMutuos(idPerfilLogueado)
        val perfilLogueado = loginDAO.obtenerPerfilPorId(idPerfilLogueado)

        val tvNombresMatch = findViewById<TextView>(R.id.tvNombresMatch)
        val ivPerro1 = findViewById<CircleImageView>(R.id.ivPerro1)
        val ivPerro2 = findViewById<CircleImageView>(R.id.ivPerro2)

        if (perfilLogueado == null) {
            // Perfil nulo: manejar error
            tvNombresMatch.text = "Perfil no encontrado"
            ivPerro1.setImageResource(R.drawable.chip_bg_gray)
            ivPerro2.setImageResource(R.drawable.chip_bg_gray)
            Toast.makeText(this, "⚠️ No se pudo cargar tu perfil.", Toast.LENGTH_SHORT).show()
            return
        }

        // Perfil logueado existe
        tvNombresMatch.text = perfilLogueado.nombre_perro
        perfilLogueado.avatar?.let { avatarBase64 ->
            loginDAO.convertBase64ToBitmap(avatarBase64)?.let {
                ivPerro1.setImageBitmap(it)
            } ?: ivPerro1.setImageResource(R.drawable.chip_bg_gray)
        } ?: ivPerro1.setImageResource(R.drawable.chip_bg_gray)

        if (matchesMutuos.isEmpty()) {
            Toast.makeText(this, "😿 Aún no tienes matches.", Toast.LENGTH_LONG).show()
        } else {
            val nombresMatches = mutableListOf<String>()

            matchesMutuos.forEachIndexed { index, idMatch ->
                val perfil = loginDAO.obtenerPerfilPorId(idMatch)
                if (perfil != null) {
                    nombresMatches.add(perfil.nombre_perro)

                    if (index == 0) {
                        // Mostrar primer match
                        perfil.avatar?.let { avatarBase64 ->
                            loginDAO.convertBase64ToBitmap(avatarBase64)?.let {
                                ivPerro2.setImageBitmap(it)
                            } ?: ivPerro2.setImageResource(R.drawable.chip_bg_gray)
                        } ?: ivPerro2.setImageResource(R.drawable.chip_bg_gray)

                        tvNombresMatch.text =
                            "${perfilLogueado.nombre_perro} ❤️ ${perfil.nombre_perro}"
                    }
                }
            }

            if (nombresMatches.isNotEmpty()) {
                Toast.makeText(
                    this,
                    "🎉 ¡Tienes match con: ${nombresMatches.joinToString(", ")}!",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(this, "⚠️ Error al obtener los perfiles.", Toast.LENGTH_SHORT).show()
            }

        }
    }
}