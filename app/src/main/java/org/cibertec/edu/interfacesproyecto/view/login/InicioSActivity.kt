package org.cibertec.edu.interfacesproyecto.view.login
import org.cibertec.edu.interfacesproyecto.view.registro.RegistroActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.LoginDAO
import org.cibertec.edu.interfacesproyecto.view.menu.MenuActivity

class InicioSActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_inicio_sactivity)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtEmail = findViewById<TextInputEditText>(R.id.Email)
        val txtNombrePerro = findViewById<TextInputEditText>(R.id.Contrasenha)
        val btnIngresar = findViewById<Button>(R.id.boton)
        val registro = findViewById<TextView>(R.id.tvRegistro)

        val loginDAO = LoginDAO(this)

        btnIngresar.setOnClickListener {
            val email = txtEmail.text.toString().trim()
            val nombrePerro = txtNombrePerro.text.toString().trim()

            if (email.isEmpty() || nombrePerro.isEmpty()) {
                Toast.makeText(this, "Por favor completa ambos campos 🐾", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ Verificar en la base de datos real
            val existe = loginDAO.verificarPerfil(email, nombrePerro)
            if (existe) {
                Toast.makeText(this, "Bienvenido a PawDate, $nombrePerro 🐶", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MenuActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Perfil no encontrado 🐕", Toast.LENGTH_SHORT).show()
            }
        }

        // Redirigir a RegistroActivity cuando se hace clic en el texto "Registrate aquí"
        registro.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }
    }
}
