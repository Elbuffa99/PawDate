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

    fun obtenerMatchesMutuos(idPerfil: Int): List<Int> {
        val db = dbHelper.readableDatabase
        val matchesMutuos = mutableListOf<Int>()

        // 1️⃣ Buscar todos los id_perfil2 a los que el usuario dio "aceptado"
        val cursor1 = db.rawQuery(
            "SELECT id_perfil2 FROM MATCHES WHERE id_perfil1 = ? AND estado = 'aceptado'",
            arrayOf(idPerfil.toString())
        )

        if (cursor1.moveToFirst()) {
            do {
                val idPerfil2 = cursor1.getInt(0)

                // 2️⃣ Verificar si ese perfil también aceptó al usuario
                val cursor2 = db.rawQuery(
                    "SELECT 1 FROM MATCHES WHERE id_perfil1 = ? AND id_perfil2 = ? AND estado = 'aceptado' LIMIT 1",
                    arrayOf(idPerfil2.toString(), idPerfil.toString())
                )

                if (cursor2.moveToFirst()) {
                    // Si hay reciprocidad, es un match mutuo ❤️
                    matchesMutuos.add(idPerfil2)
                }

                cursor2.close()
            } while (cursor1.moveToNext())
        }

        cursor1.close()
        db.close()

        return matchesMutuos
    }

}
