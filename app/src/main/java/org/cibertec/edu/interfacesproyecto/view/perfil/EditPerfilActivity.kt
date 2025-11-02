package org.cibertec.edu.interfacesproyecto.view.perfil

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import org.cibertec.edu.interfacesproyecto.view.perfil.PerfilActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import de.hdodenhof.circleimageview.CircleImageView
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.LoginDAO
import org.cibertec.edu.interfacesproyecto.model.entidades.Perfil
import org.cibertec.edu.interfacesproyecto.view.menu.MenuActivity
import android.util.Base64
import java.io.ByteArrayOutputStream
import android.graphics.ImageDecoder


class EditPerfilActivity : AppCompatActivity() {

    private lateinit var loginDAO: LoginDAO
    private var avatarBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_edit_perfil)
        loginDAO = LoginDAO(this)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        val ivFotoPerfil = findViewById<CircleImageView>(R.id.ivFotoPerfil)
        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etTelefono = findViewById<EditText>(R.id.etTelefono)
        val etDescripcion = findViewById<EditText>(R.id.etDescripcion)
        val btnGuardarCambios = findViewById<Button>(R.id.btnGuardarCambios)
        val btnCambiarFoto = findViewById<Button>(R.id.btnCambiarFoto)

        // Obtener el id del perfil desde la sesión
        val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
        val idPerfil = prefs.getInt("id_perfil", -1)

        var perfilActual: Perfil? = null

        if (idPerfil != -1) {
            val perfil = loginDAO.obtenerPerfilPorId(idPerfil)
            perfil?.let {

                perfilActual = it
                // Nombre del perro
                etNombre.setText(it.nombre_perro)
                // Email
                etEmail.setText(it.email)
                // Teléfono
                etTelefono.setText(it.telefono)
                // Descripción o busca
                etDescripcion.setText(it.busca)

                // Cargar avatar si existe (Base64 -> Bitmap)
                it.avatar?.let { base64String ->
                    val bitmap = loginDAO.convertBase64ToBitmap(base64String)
                    if (bitmap != null) {
                        ivFotoPerfil.setImageBitmap(bitmap)
                    }
                }
                // 🔴 NUEVO: Lanzar selector de imagen
                val pickImageLauncher = registerForActivityResult(
                    ActivityResultContracts.StartActivityForResult()
                ) { result ->
                    if (result.resultCode == Activity.RESULT_OK) {
                        val data: Intent? = result.data
                        val imageUri: Uri? = data?.data
                        if (imageUri != null) {
                            val bitmap = uriToBitmap(imageUri)
                            ivFotoPerfil.setImageBitmap(bitmap)
                            avatarBase64 = bitmapToBase64(bitmap) // 🔴 Guardamos la nueva foto en Base64
                        }
                    }
                }

                btnCambiarFoto.setOnClickListener {
                    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    pickImageLauncher.launch(intent)
                }

                // Botón guardar cambios
                val btnGuardarCambios = findViewById<Button>(R.id.btnGuardarCambios)
                btnGuardarCambios.setOnClickListener {
                    // Aquí implementas la lógica para actualizar la BD
                    // loginDAO.actualizarPerfil(...)
                }

                val BtnMenu = findViewById<ImageView>(R.id.btnBack)
                BtnMenu.setOnClickListener {
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)
                    finish() // opcional: cierra la actividad actual
                }

            }
        }

        // 4️⃣ Configurar botón de guardar cambios
        btnGuardarCambios.setOnClickListener {
            val perfil = perfilActual
            if (perfil == null) {
                android.widget.Toast.makeText(
                    this,
                    "Error: no se cargó el perfil",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Leer los valores de los EditText
            val nombre = etNombre.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val telefono = etTelefono.text.toString().trim()
            val descripcion = etDescripcion.text.toString().trim()

            // Validación básica
            if (nombre.isEmpty() || email.isEmpty() || telefono.isEmpty() || descripcion.isEmpty()) {
                android.widget.Toast.makeText(
                    this,
                    "Por favor completa todos los campos",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            // Crear objeto actualizado
            val perfilActualizado = perfil.copy(
                nombre_perro = nombre,
                email = email,
                telefono = telefono,
                busca = descripcion,
                avatar = avatarBase64 ?: perfil.avatar
            )

            // Guardar en la BD
            val exito = loginDAO.actualizarPerfil(perfilActualizado)

            if (exito) {
                android.widget.Toast.makeText(
                    this,
                    "Cambios guardados correctamente",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
                perfilActual = perfilActualizado

                // 🔽 Ir a PerfilActivity después de guardar
                val intent = Intent(this, PerfilActivity::class.java)
                startActivity(intent)
                finish() // opcional, para que no pueda volver atrás

            } else {
                android.widget.Toast.makeText(
                    this,
                    "Error al guardar cambios",
                    android.widget.Toast.LENGTH_SHORT
                ).show()
            }




        }
    }
    // 🔴 NUEVO: Métodos auxiliares para manejar imagen
    private fun uriToBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(this.contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        } else {
            @Suppress("DEPRECATION")
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        }
    }

    private fun bitmapToBase64(bitmap: Bitmap): String {
        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        val byteArray = outputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.DEFAULT)
    }
}