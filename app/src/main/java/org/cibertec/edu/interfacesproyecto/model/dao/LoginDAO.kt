package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.util.Log
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper
import org.cibertec.edu.interfacesproyecto.model.entidades.Habito
import org.cibertec.edu.interfacesproyecto.model.entidades.Perfil
import org.cibertec.edu.interfacesproyecto.model.entidades.Personalidad
import java.sql.Date

class LoginDAO(context: Context) {
    private val dbHelper = PawDateDBHelper(context)

    fun verificarPerfil(email: String, nombrePerro: String): Boolean {
        val db = dbHelper.readableDatabase
        Log.d("LoginDAO", "📁 Ruta de la BD: ${db.path}") // <-- compara esto con PerfilDAO

        var existe = false
        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM PERFILES WHERE email=? AND nombre_perro=?",
            arrayOf(email, nombrePerro)
        )
        if (cursor.moveToFirst()) {
            existe = true
        }
        cursor.close()
        db.close()
        return existe
    }

    fun obtenerIdPerfil(email: String): Int {
        val db = dbHelper.readableDatabase
        var idPerfil = -1

        // Verificamos que exista una columna "id_perfil" (ajusta si tu columna tiene otro nombre)
        val cursor = db.rawQuery(
            "SELECT id_perfil FROM PERFILES WHERE email=?",
            arrayOf(email)
        )

        if (cursor.moveToFirst()) {
            idPerfil = cursor.getInt(0)
        }

        cursor.close()
        db.close()
        return idPerfil
    }

    fun obtenerPerfilPorId(idPerfil: Int): Perfil? {
        val db = dbHelper.readableDatabase
        var perfil: Perfil? = null

        val cursor = db.rawQuery(
            "SELECT * FROM PERFILES WHERE id_perfil=?",
            arrayOf(idPerfil.toString())
        )

        if (cursor.moveToFirst()) {

            val fechaNacMillis = cursor.getLong(cursor.getColumnIndexOrThrow("fecha_nacimiento"))


            perfil = Perfil(
                id_perfil = cursor.getInt(cursor.getColumnIndexOrThrow("id_perfil")),
                email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                nombre_perro = cursor.getString(cursor.getColumnIndexOrThrow("nombre_perro")),
                fecha_nacimiento = Date(fechaNacMillis),
                genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                busca = cursor.getString(cursor.getColumnIndexOrThrow("busca")),
                relaciones = cursor.getString(cursor.getColumnIndexOrThrow("relaciones")),
                avatar = cursor.getString(cursor.getColumnIndexOrThrow("avatar"))
            )
        }

        cursor.close()
        db.close()
        return perfil
    }

    // Función para traer los hábitos de un perfil
    fun obtenerHabitosPorPerfil(idPerfil: Int): Habito? {
        val db = dbHelper.readableDatabase
        var habito: Habito? = null

        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM HABITOS WHERE id_perfil=?",
            arrayOf(idPerfil.toString())
        )

        if (cursor.moveToFirst()) {
            habito = Habito(
                id_habito = cursor.getInt(cursor.getColumnIndexOrThrow("id_habito")),
                id_perfil = cursor.getInt(cursor.getColumnIndexOrThrow("id_perfil")),
                nivel_energia = cursor.getString(cursor.getColumnIndexOrThrow("nivel_energia")),
                frecuencia_paseos = cursor.getString(cursor.getColumnIndexOrThrow("frecuencia_paseos")),
                sociabilidad = cursor.getString(cursor.getColumnIndexOrThrow("sociabilidad")),
                alimentacion = cursor.getString(cursor.getColumnIndexOrThrow("alimentacion")),
                horarios_descanso = cursor.getString(cursor.getColumnIndexOrThrow("horarios_descanso"))
            )
        }

        cursor.close()
        db.close()
        return habito
    }

    // Función para traer las personalidades de un perfil
    fun obtenerPersonalidadPorPerfil(idPerfil: Int): Personalidad? {
        val db = dbHelper.readableDatabase
        var personalidad: Personalidad? = null

        val cursor: Cursor = db.rawQuery(
            "SELECT * FROM PERSONALIDADES WHERE id_perfil=?",
            arrayOf(idPerfil.toString())
        )

        if (cursor.moveToFirst()) {
            personalidad = Personalidad(
                id_personalidad = cursor.getInt(cursor.getColumnIndexOrThrow("id_personalidad")),
                id_perfil = cursor.getInt(cursor.getColumnIndexOrThrow("id_perfil")),
                comportamiento = cursor.getString(cursor.getColumnIndexOrThrow("comportamiento")),
                entorno = cursor.getString(cursor.getColumnIndexOrThrow("entorno")),
                interaccion_social = cursor.getString(cursor.getColumnIndexOrThrow("interaccion_social"))
            )
        }

        cursor.close()
        db.close()
        return personalidad
    }

    fun convertBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = android.util.Base64.decode(base64Str, android.util.Base64.DEFAULT)
            android.graphics.BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            null
        }
    }

    fun actualizarPerfil(perfil: Perfil): Boolean {
        val db = dbHelper.writableDatabase
        return try {
            val values = android.content.ContentValues().apply {
                put("nombre_perro", perfil.nombre_perro)
                put("email", perfil.email)
                put("telefono", perfil.telefono)
                put("busca", perfil.busca)
                put("avatar", perfil.avatar)
            }
            val rows = db.update(
                "PERFILES",
                values,
                "id_perfil=?",
                arrayOf(perfil.id_perfil.toString())
            )
            db.close()
            rows > 0
        } catch (e: Exception) {
            e.printStackTrace()
            db.close()
            false
        }
    }

    fun listarPerfilesExcepto(idPerfilActual: Int): List<Perfil> {
        val db = dbHelper.readableDatabase
        val perfiles = mutableListOf<Perfil>()

        val cursor = db.rawQuery(
            "SELECT * FROM PERFILES WHERE id_perfil != ?",
            arrayOf(idPerfilActual.toString())
        )

        if (cursor.moveToFirst()) {
            do {
                val fechaNacMillis = cursor.getLong(cursor.getColumnIndexOrThrow("fecha_nacimiento"))
                val perfil = Perfil(
                    id_perfil = cursor.getInt(cursor.getColumnIndexOrThrow("id_perfil")),
                    email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    telefono = cursor.getString(cursor.getColumnIndexOrThrow("telefono")),
                    nombre_perro = cursor.getString(cursor.getColumnIndexOrThrow("nombre_perro")),
                    fecha_nacimiento = java.sql.Date(fechaNacMillis),
                    genero = cursor.getString(cursor.getColumnIndexOrThrow("genero")),
                    busca = cursor.getString(cursor.getColumnIndexOrThrow("busca")),
                    relaciones = cursor.getString(cursor.getColumnIndexOrThrow("relaciones")),
                    avatar = cursor.getString(cursor.getColumnIndexOrThrow("avatar"))
                )
                perfiles.add(perfil)
            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()
        return perfiles
    }

    fun obtenerAvatarBase64PorId(idPerfil: Int): String? {
        val db = dbHelper.readableDatabase
        var avatarBase64: String? = null
        val cursor = db.rawQuery(
            "SELECT avatar FROM PERFILES WHERE id_perfil = ?",
            arrayOf(idPerfil.toString())
        )
        if (cursor.moveToFirst()) {
            avatarBase64 = cursor.getString(0)
        }
        cursor.close()
        db.close()
        return avatarBase64
    }

    fun existePerfilPorId(idPerfil: Int): Boolean {
        val db = dbHelper.readableDatabase
        var existe = false
        val cursor = db.rawQuery(
            "SELECT 1 FROM PERFILES WHERE id_perfil = ? LIMIT 1",
            arrayOf(idPerfil.toString())
        )
        if (cursor.moveToFirst()) {
            existe = true
        }
        cursor.close()
        db.close()
        return existe
    }


}
