package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.util.Log
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper

/**
 * Maneja las operaciones CRUD de la tabla HABITOS.
 * Cada perfil tiene un conjunto de hábitos asociados (1 a 1 o 1 a muchos según diseño).
 */
class HabitosDAO(private val context: Context) {

    private val dbHelper = PawDateDBHelper(context)
    private val session = SessionManager(context)

    /**
     * Inserta los hábitos seleccionados para el perfil actual.
     * Retorna true si se insertó correctamente.
     */
    fun insertarHabitos(
        nivelEnergia: String,
        frecuenciaPaseos: String,
        sociabilidad: String,
        alimentacion: String,
        horariosDescanso: String
    ): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        val idPerfil = session.obtenerIdPerfil()

        if (idPerfil == -1) {
            Log.e("HabitosDAO", "❌ No se encontró ID de perfil en sesión.")
            return false
        }

        try {
            val valores = ContentValues().apply {
                put("id_perfil", idPerfil)
                put("nivel_energia", nivelEnergia)
                put("frecuencia_paseos", frecuenciaPaseos)
                put("sociabilidad", sociabilidad)
                put("alimentacion", alimentacion)
                put("horarios_descanso", horariosDescanso)
            }

            val id = db.insertOrThrow("HABITOS", null, valores)
            if (id > 0) {
                Log.d("HabitosDAO", "✅ Hábitos insertados correctamente (id=$id)")
                exito = true
            }

        } catch (e: SQLException) {
            Log.e("HabitosDAO", "❌ Error SQL al insertar hábitos", e)
        } catch (e: Exception) {
            Log.e("HabitosDAO", "❌ Error inesperado al insertar hábitos", e)
        } finally {
            db.close()
        }

        return exito
    }

    /**
     * Obtiene los hábitos de un perfil dado.
     */
    fun obtenerHabitosPorPerfil(idPerfil: Int): Map<String, String>? {
        val db = dbHelper.readableDatabase
        var resultado: MutableMap<String, String>? = null

        try {
            val cursor = db.rawQuery(
                "SELECT nivel_energia, frecuencia_paseos, sociabilidad, alimentacion, horarios_descanso FROM HABITOS WHERE id_perfil = ?",
                arrayOf(idPerfil.toString())
            )

            if (cursor.moveToFirst()) {
                resultado = mutableMapOf(
                    "nivel_energia" to cursor.getString(0),
                    "frecuencia_paseos" to cursor.getString(1),
                    "sociabilidad" to cursor.getString(2),
                    "alimentacion" to cursor.getString(3),
                    "horarios_descanso" to cursor.getString(4)
                )
            }
            cursor.close()

        } catch (e: Exception) {
            Log.e("HabitosDAO", "❌ Error al obtener hábitos", e)
        } finally {
            db.close()
        }

        return resultado
    }

    /**
     * Elimina los hábitos de un perfil (si se reinicia el flujo, por ejemplo).
     */
    fun eliminarHabitosPorPerfil(idPerfil: Int): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        try {
            val filas = db.delete("HABITOS", "id_perfil = ?", arrayOf(idPerfil.toString()))
            if (filas > 0) {
                Log.d("HabitosDAO", "🧹 Hábitos eliminados para perfil ID=$idPerfil")
                exito = true
            }
        } catch (e: Exception) {
            Log.e("HabitosDAO", "❌ Error al eliminar hábitos", e)
        } finally {
            db.close()
        }
        return exito
    }
}
