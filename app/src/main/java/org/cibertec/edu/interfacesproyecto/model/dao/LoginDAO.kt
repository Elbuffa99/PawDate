package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.Context
import android.database.Cursor
import android.util.Log
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper
import org.cibertec.edu.interfacesproyecto.model.entidades.Perfil
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

}
