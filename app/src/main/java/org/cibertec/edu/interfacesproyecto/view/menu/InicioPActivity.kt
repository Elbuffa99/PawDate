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
            var perfilIdFinal = idPerfil

            // 🔴 Si el idPerfil es 1, lo incrementamos y lo guardamos como "buscandopulgas"
            if (idPerfil == 1) {
                perfilIdFinal = idPerfil + 1

                val editor = prefs.edit()
                editor.putInt("id_perfil", perfilIdFinal)
                editor.putString("sesion_nombre", "buscandopulgas") // 🔴 Marca especial de sesión
                editor.apply()
            }

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
            val sesionNombre = prefs.getString("sesion_nombre", null)
            var idPerfil = prefs.getInt("id_perfil", -1)
            val idLogin = prefs.getInt("id_login", -1)

            if (sesionNombre == "buscandopulgas" && idPerfil != -1) {
                // Calcular el siguiente perfil
                var nuevoIdPerfil = idPerfil + 1

                // Verificar si existe ese perfil
                val existePerfil = loginDAO.existePerfilPorId(nuevoIdPerfil)

                // Si no existe, decidir reinicio según el id_login
                if (!existePerfil) {
                    nuevoIdPerfil = if (idLogin == 1) 2 else 1
                }

                // Guardar el nuevo ID en SharedPreferences
                val editor = prefs.edit()
                editor.putInt("id_perfil", nuevoIdPerfil)
                editor.apply()

                // Obtener y mostrar el avatar del nuevo perfil si existe
                val avatarBase64 = loginDAO.obtenerAvatarBase64PorId(nuevoIdPerfil)
                if (!avatarBase64.isNullOrEmpty()) {
                    val bitmap = convertBase64ToBitmap(avatarBase64)
                    if (bitmap != null) {
                        imgAvatar.setImageBitmap(bitmap)
                        Toast.makeText(this, "¡Te encanta! 🐾💚", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Error al convertir imagen 😿", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "No hay imagen para este perfil 😺", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Inicia sesión como buscandopulgas", Toast.LENGTH_SHORT).show()
            }
        }

        btnReject.setOnClickListener {
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            val sesionNombre = prefs.getString("sesion_nombre", null)
            var idPerfil = prefs.getInt("id_perfil", -1)
            val idLogin = prefs.getInt("id_login", -1)

            if (sesionNombre == "buscandopulgas" && idPerfil != -1) {
                // Calcular el siguiente perfil
                var nuevoIdPerfil = idPerfil + 1

                // Verificar si el perfil existe
                val existePerfil = loginDAO.existePerfilPorId(nuevoIdPerfil)

                // Si no existe, reiniciar según el login
                if (!existePerfil) {
                    nuevoIdPerfil = if (idLogin == 1) 2 else 1
                }

                // Guardar el nuevo perfil
                val editor = prefs.edit()
                editor.putInt("id_perfil", nuevoIdPerfil)
                editor.apply()

                // Cargar el nuevo avatar
                val avatarBase64 = loginDAO.obtenerAvatarBase64PorId(nuevoIdPerfil)
                if (!avatarBase64.isNullOrEmpty()) {
                    val bitmap = convertBase64ToBitmap(avatarBase64)
                    if (bitmap != null) {
                        imgAvatar.setImageBitmap(bitmap)
                        Toast.makeText(this, "💔 No fue para ti... ¡Sigue buscando!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "⚠️ Error al mostrar la imagen.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this, "😿 Este perfil no tiene imagen.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "🔒 Inicia sesión como *buscandopulgas* para usar esta función.", Toast.LENGTH_LONG).show()
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