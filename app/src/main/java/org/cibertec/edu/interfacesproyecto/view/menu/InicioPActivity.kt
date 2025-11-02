package org.cibertec.edu.interfacesproyecto.view.menu

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.LoginDAO
import android.util.Base64
import android.widget.ImageButton
import android.widget.Toast
import org.cibertec.edu.interfacesproyecto.model.dao.MatchDAO
import org.cibertec.edu.interfacesproyecto.model.entidades.Match

class InicioPActivity : AppCompatActivity() {

    private lateinit var loginDAO: LoginDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_pactivity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicializar DAO
        loginDAO = LoginDAO(this)

        // Referencia al ImageView del avatar
        val imgAvatar = findViewById<ImageView>(R.id.ivDogImage)


        val BtnMenu = findViewById<ImageView>(R.id.iconMenu)

        val btnAccept = findViewById<ImageButton>(R.id.btnAccept)

        val btnReject = findViewById<ImageButton>(R.id.btnReject)


        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val idPerfil = prefs.getInt("id_perfil", -1)

        if (idPerfil != -1) {
            var perfilIdFinal = 1

            // 🔴 Si el idPerfil es 1, lo incrementamos y lo guardamos como "buscandopulgas"
            if (idPerfil == 1) {
                perfilIdFinal = idPerfil + 1
            }
            val existePerfil = loginDAO.existePerfilPorId(perfilIdFinal)

            // Si no existe, decidir reinicio según el id_login
            if (!existePerfil) {
                perfilIdFinal = if (idPerfil == 1) 2 else 1
            }

            val editor = prefs.edit()
            editor.putInt("buscandopulgas", perfilIdFinal)
            editor.apply()
            // Obtener avatar en Base64 desde la BD
            val avatarBase64 = loginDAO.obtenerAvatarBase64PorId(perfilIdFinal)

            if (avatarBase64 != null && avatarBase64.isNotEmpty()) {
                val bitmap = convertBase64ToBitmap(avatarBase64)
                imgAvatar.setImageBitmap(bitmap)
            } else {
                // Si no tiene imagen, mostrar una por defecto

            }
        }



        BtnMenu.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // opcional: cierra la actividad actual
        }

        btnAccept.setOnClickListener {
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            val idPerfilSesion = prefs.getInt("id_perfil", -1)        // 🟢 El perro logueado
            var idBuscandoPulgas = prefs.getInt("buscandopulgas", -1) // 🟢 El perro mostrado actualmente

            if (idPerfilSesion != -1 && idBuscandoPulgas != -1) {
                val matchDAO = MatchDAO(this)

                // 🐾 Registrar el match
                val match = Match(
                    id_perfil1 = idPerfilSesion,      // El que da like (logueado)
                    id_perfil2 = idBuscandoPulgas,    // El mostrado
                    estado = "aceptado"
                )
                matchDAO.registrarMatch(match)
                Toast.makeText(this, "¡Te encanta! 🐾💚", Toast.LENGTH_SHORT).show()

                // 👉 Pasar al siguiente perfil
                var nuevoIdBuscandoPulgas = idBuscandoPulgas + 1

                // Verificar si existe ese perfil
                val existePerfil = loginDAO.existePerfilPorId(nuevoIdBuscandoPulgas)

                // Si no existe, reiniciar (como en el otro método)
                if (!existePerfil) {
                    nuevoIdBuscandoPulgas = if (idBuscandoPulgas == 1) 2 else 1
                }

                // Guardar el nuevo valor de 'buscandopulgas'
                val editor = prefs.edit()
                editor.putInt("buscandopulgas", nuevoIdBuscandoPulgas)
                editor.apply()

                // 🖼️ Obtener y mostrar el nuevo avatar
                val avatarBase64 = loginDAO.obtenerAvatarBase64PorId(nuevoIdBuscandoPulgas)
                if (!avatarBase64.isNullOrEmpty()) {
                    val bitmap = convertBase64ToBitmap(avatarBase64)
                    imgAvatar.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, "No hay imagen para este perfil 😺", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Sesión inválida. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show()
            }
        }


        btnReject.setOnClickListener {
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            var idBuscandoPulgas = prefs.getInt("buscandopulgas", -1)

            if (idBuscandoPulgas != -1) {
                Toast.makeText(this, "💔 No fue para ti... ¡Sigue buscando!", Toast.LENGTH_SHORT).show()

                // 👉 Pasar al siguiente perfil (igual que aceptar)
                var nuevoIdBuscandoPulgas = idBuscandoPulgas + 1
                val existePerfil = loginDAO.existePerfilPorId(nuevoIdBuscandoPulgas)

                if (!existePerfil) {
                    nuevoIdBuscandoPulgas = if (idBuscandoPulgas == 1) 2 else 1
                }

                // Guardar el nuevo perfil mostrado
                prefs.edit().putInt("buscandopulgas", nuevoIdBuscandoPulgas).apply()

                // 🖼️ Mostrar la nueva imagen
                val avatarBase64 = loginDAO.obtenerAvatarBase64PorId(nuevoIdBuscandoPulgas)
                if (!avatarBase64.isNullOrEmpty()) {
                    val bitmap = convertBase64ToBitmap(avatarBase64)
                    imgAvatar.setImageBitmap(bitmap)
                } else {
                    Toast.makeText(this, "No hay imagen para este perfil 😺", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Sesión inválida. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun convertBase64ToBitmap(base64String: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64String, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}