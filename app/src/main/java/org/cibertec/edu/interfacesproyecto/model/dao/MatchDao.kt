package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper
import org.cibertec.edu.interfacesproyecto.model.entidades.Match

class MatchDAO(context: Context) {

    private val dbHelper = PawDateDBHelper(context)

    fun registrarMatch(match: Match): Boolean {
        val db: SQLiteDatabase = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("id_perfil1", match.id_perfil1)
            put("id_perfil2", match.id_perfil2)
            put("estado", match.estado)
            put("fecha_match", match.fecha_match)
        }

        val result = db.insert("MATCHES", null, values)
        db.close()
        return result != -1L
    }
}
