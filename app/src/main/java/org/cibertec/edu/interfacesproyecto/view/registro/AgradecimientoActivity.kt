package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.model.dao.PerfilDAO
import org.cibertec.edu.interfacesproyecto.view.menu.MenuActivity

class AgradecimientoActivity : AppCompatActivity() {

    private lateinit var imgAvatarFinal: ImageView
    private lateinit var tvCuenta: TextView
    private lateinit var tvBuscando: TextView
    private lateinit var btnIrMenu: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_agradecimiento)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgAvatarFinal = findViewById(R.id.ImgAvatarFinal)
        tvCuenta = findViewById(R.id.TvCuenta)
        tvBuscando = findViewById(R.id.TvBuscando)
        btnIrMenu = findViewById(R.id.BIrMenu)

        mostrarAvatar()

        // 🟢 Espera automática de 3 segundos y redirige al menú
        Handler(Looper.getMainLooper()).postDelayed({
            irAlMenu()
        }, 3000)

        // 🟢 También permitir que el usuario pulse el botón manualmente
        btnIrMenu.setOnClickListener {
            irAlMenu()
        }
    }

    private fun mostrarAvatar() {
        val session = SessionManager(this)
        val idPerfil = session.obtenerIdPerfil()
        Log.d("AgradecimientoActivity", "🔍 ID de perfil obtenido: $idPerfil")

        if (idPerfil == -1) {
            Log.e("AgradecimientoActivity", "❌ No hay ID de perfil en sesión. No se mostrará avatar.")
            return
        }

        val perfilDAO = PerfilDAO(this)
        val perfil = perfilDAO.obtenerPerfilPorId(idPerfil)

        if (perfil == null) {
            Log.e("AgradecimientoActivity", "⚠️ No se encontró ningún perfil en BD con ID=$idPerfil")
            return
        }

        // Mostrar avatar
        perfil["avatar"]?.let { avatarBase64 ->
            try {
                val bytes = Base64.decode(avatarBase64, Base64.DEFAULT)
                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                imgAvatarFinal.setImageBitmap(bitmap)
            } catch (e: Exception) {
                Log.e("AgradecimientoActivity", "❌ Error al decodificar avatar", e)
            }
        }

        tvCuenta.text = "¡Gracias por crear tu cuenta, ${perfil["nombre_perro"] ?: "perrito"}!"
        tvBuscando.text = "Estamos buscando posibles compañeros perrunos..."
    }


    private fun irAlMenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        finish()
    }
}
