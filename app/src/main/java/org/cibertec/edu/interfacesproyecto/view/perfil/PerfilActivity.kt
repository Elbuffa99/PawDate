package org.cibertec.edu.interfacesproyecto.view.perfil

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.LoginDAO
import org.cibertec.edu.interfacesproyecto.view.login.InicioSActivity
import org.cibertec.edu.interfacesproyecto.view.menu.MenuActivity

class PerfilActivity : AppCompatActivity() {

    private lateinit var loginDAO: LoginDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_perfil)

        loginDAO = LoginDAO(this)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btnBack = findViewById<ImageView>(R.id.btnBack)

        val btnCierraSesion = findViewById<Button>(R.id.btnCerrarSesion)

        val btneditar = findViewById<Button>(R.id.btnEditarPerfil)

        val tvNombrePerrito = findViewById<TextView>(R.id.tvNombrePerrito)

        val tvDescripcion = findViewById<TextView>(R.id.tvDescripcion)

        val tvHabitos = findViewById<TextView>(R.id.tvHabitos)

        val ivFotoPerfil = findViewById<de.hdodenhof.circleimageview.CircleImageView>(R.id.ivFotoPerfil)

        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)

        val idPerfil = prefs.getInt("id_perfil", -1)

        // 2️⃣ Obtener perfil y actualizar UI
        if (idPerfil != -1) {
            val perfil = loginDAO.obtenerPerfilPorId(idPerfil)
            val habito = loginDAO.obtenerHabitosPorPerfil(idPerfil)
            val personalidad = loginDAO.obtenerPersonalidadPorPerfil(idPerfil)
            perfil?.let {
                tvNombrePerrito.text = it.nombre_perro

                // Cargar avatar si existe
                it.avatar?.let { base64Str ->
                    val bitmap = loginDAO.convertBase64ToBitmap(base64Str)
                    if (bitmap != null) {
                        ivFotoPerfil.setImageBitmap(bitmap) // 🔴 Asignamos la imagen del perfil
                    }
                }

                // Reemplazamos el texto de tvDescripcion con los datos combinados
                val comportamiento = personalidad?.comportamiento ?: "alegre"
                val sociabilidad = habito?.sociabilidad ?: "sociable"
                val interaccion = personalidad?.interaccion_social ?: "amistoso"

                tvDescripcion.text = "Soy un perrito $comportamiento, $sociabilidad y $interaccion. 🐕✨"

                val frecuenciaPaseos = habito?.frecuencia_paseos ?: "paseos diarios"
                val nivelEnergia = habito?.nivel_energia ?: "alegre y activo"
                val alimentacion = habito?.alimentacion ?: "comida variada"
                val comportamiento2 = personalidad?.comportamiento ?: "amigable"
                val entorno = personalidad?.entorno ?: "tranquilo"

                tvHabitos.text = "- Paseos: $frecuenciaPaseos 🦮\n" +
                        "- Nivel de energía: $nivelEnergia 😴\n" +
                        "- Alimentación: $alimentacion 🍗\n" +
                        "- Personalidad: $comportamiento2 y $entorno 🐾"

            }
        }


        btnBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            // Obtener hábitos
            val habito = loginDAO.obtenerHabitosPorPerfil(idPerfil)

            // Obtener personalidad
            val personalidad = loginDAO.obtenerPersonalidadPorPerfil(idPerfil)
            startActivity(intent)
            finish() // opcional: cierra la actividad actual
        }

        btnCierraSesion.setOnClickListener {
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            val editor = prefs.edit()
            editor.clear()  // Limpia todo lo que haya guardado
            editor.apply()
            startActivity(Intent(this, InicioSActivity::class.java))
            finish()
        }

        btneditar.setOnClickListener {

            startActivity(Intent(this, EditPerfilActivity::class.java))

        }
    }
}