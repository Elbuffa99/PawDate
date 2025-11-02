package org.cibertec.edu.interfacesproyecto.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.view.registro.RegistroActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🟢 Botón "Registrarme"
        val btnRegistrarme = findViewById<Button>(R.id.BRegistrate)
        btnRegistrarme.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }

        // 🟡 Botón "Iniciar sesión"
        val btnIniciar = findViewById<Button>(R.id.BIniciar)
        btnIniciar.setOnClickListener {
            val intent = Intent(this, InicioSActivity::class.java)
            startActivity(intent)
        }
    }
}
