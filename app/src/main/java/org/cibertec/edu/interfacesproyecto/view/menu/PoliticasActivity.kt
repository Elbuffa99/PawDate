package org.cibertec.edu.interfacesproyecto.view.menu

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.cibertec.edu.interfacesproyecto.R

class PoliticasActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.privacy_policy)


        val btnBack = findViewById<ImageView>(R.id.btnBack)
        btnBack.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // opcional: cierra la actividad actual
        }

    }
}
