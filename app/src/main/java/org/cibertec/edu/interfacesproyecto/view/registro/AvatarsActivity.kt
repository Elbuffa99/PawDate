package org.cibertec.edu.interfacesproyecto.view.registro

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import org.cibertec.edu.interfacesproyecto.R
import org.cibertec.edu.interfacesproyecto.model.dao.PerfilDAO
import java.io.ByteArrayOutputStream

class AvatarsActivity : AppCompatActivity() {

    private lateinit var imgPerfil: ImageView
    private lateinit var btnSiguiente: Button
    private val PICK_IMAGE = 100
    private var imagenBase64: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_avatars)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        imgPerfil = findViewById(R.id.ImgPerfil)
        btnSiguiente = findViewById(R.id.BSiguiente)

        imgPerfil.setOnClickListener {
            abrirGaleria()
        }

        btnSiguiente.setOnClickListener {
            if (imagenBase64 == null) {
                Toast.makeText(this, "Selecciona una imagen primero.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val perfilDAO = PerfilDAO(this)
            val exito = perfilDAO.actualizarAvatar(imagenBase64!!)

            if (exito) {
                Toast.makeText(this, "Avatar guardado correctamente.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, AgradecimientoActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, "Error al guardar avatar.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun abrirGaleria() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_IMAGE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri: Uri? = data.data
            if (imageUri != null) {
                val bitmap = obtenerBitmap(imageUri)
                imgPerfil.setImageBitmap(bitmap)
                imagenBase64 = convertirABase64(bitmap)
            }
        }
    }

    private fun obtenerBitmap(uri: Uri): Bitmap {
        return if (Build.VERSION.SDK_INT < 28) {
            MediaStore.Images.Media.getBitmap(contentResolver, uri)
        } else {
            val source = ImageDecoder.createSource(contentResolver, uri)
            ImageDecoder.decodeBitmap(source)
        }
    }

    private fun convertirABase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val bytes = stream.toByteArray()
        return Base64.encodeToString(bytes, Base64.DEFAULT)
    }
}
