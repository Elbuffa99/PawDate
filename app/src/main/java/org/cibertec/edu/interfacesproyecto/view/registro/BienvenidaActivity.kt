package org.cibertec.edu.interfacesproyecto.view.registro

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R

class BienvenidaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bienvenida)

        // Ajuste visual de los insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Botón "DE ACUERDO"
        val btnAcuerdo = findViewById<Button>(R.id.BAcuerdo)

        btnAcuerdo.setOnClickListener {
            // Simplemente avanzamos a la siguiente pantalla (NombreActivity)
            val intent = Intent(this, NombreActivity::class.java)
            startActivity(intent)
            finish() // opcional, si no quieres que vuelva atrás
        }
    }
}
