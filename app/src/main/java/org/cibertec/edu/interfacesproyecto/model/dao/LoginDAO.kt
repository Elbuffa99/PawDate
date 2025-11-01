package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.Context
import android.database.Cursor
import android.util.Log
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper

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
}
