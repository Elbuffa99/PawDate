    package org.cibertec.edu.interfacesproyecto.view.menu

    import android.content.Intent
    import android.os.Bundle
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.drawerlayout.widget.DrawerLayout
    import com.google.android.material.navigation.NavigationView
    import org.cibertec.edu.interfacesproyecto.R
    import org.cibertec.edu.interfacesproyecto.view.perfil.PerfilActivity

    class MenuActivity : AppCompatActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            enableEdgeToEdge()
            setContentView(R.layout.activity_menu)
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.dlayMenu)) { v, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
                insets
            }

            val drawerLayout = findViewById<DrawerLayout>(R.id.dlayMenu)
            val navView = findViewById<NavigationView>(R.id.nvMenu)

            // Manejo de clics en el menú
            navView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_home -> {
                        // Ejemplo: volver a inicio
                        startActivity(Intent(this, InicioPActivity::class.java))
                    }

                    R.id.nav_profile -> {
                        // Ejemplo: ir a perfil
                        startActivity(Intent(this, PerfilActivity::class.java))
                    }

                    R.id.nav_match -> {
                        // Ejemplo: ir a mis match
                        startActivity(Intent(this, MatchEncuentroActivity::class.java))
                    }

                    R.id.nav_privacy -> {
                        // 👉 Redirigir a la vista de Políticas
                        startActivity(Intent(this, PoliticasActivity::class.java))
                    }

                    R.id.nav_logout -> {
                        // Cerrar sesión, según tu lógica
                        finish()
                    }
                }

                drawerLayout.closeDrawers() // Cierra el menú lateral
                true
            }

        }
    }