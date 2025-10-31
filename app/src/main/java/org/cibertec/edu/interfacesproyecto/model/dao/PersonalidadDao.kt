package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.util.Log
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper

/**
 * DAO que gestiona las operaciones CRUD para la tabla PERSONALIDADES.
 * Cada perfil puede tener una única personalidad definida.
 */
class PersonalidadDAO(private val context: Context) {

    private val dbHelper = PawDateDBHelper(context)
    private val session = SessionManager(context)

    /**
     * Inserta una personalidad asociada al perfil actual.
     * Retorna true si la inserción fue exitosa.
     */
    fun insertarPersonalidad(
        comportamiento: String,
        entorno: String,
        interaccionSocial: String
    ): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        val idPerfil = session.obtenerIdPerfil()

        if (idPerfil == -1) {
            Log.e("PersonalidadDAO", "❌ No se encontró ID de perfil en sesión.")
            return false
        }

        try {
            val valores = ContentValues().apply {
                put("id_perfil", idPerfil)
                put("comportamiento", comportamiento)
                put("entorno", entorno)
                put("interaccion_social", interaccionSocial)
            }

            val id = db.insertOrThrow("PERSONALIDADES", null, valores)
            if (id > 0) {
                Log.d("PersonalidadDAO", "✅ Personalidad insertada correctamente (id=$id)")
                exito = true
            }

        } catch (e: SQLException) {
            Log.e("PersonalidadDAO", "❌ Error SQL al insertar personalidad", e)
        } catch (e: Exception) {
            Log.e("PersonalidadDAO", "❌ Error inesperado al insertar personalidad", e)
        } finally {
            db.close()
        }

        return exito
    }

    /**
     * Obtiene la personalidad asociada a un perfil.
     * Devuelve un mapa con los valores o null si no existe.
     */
    fun obtenerPersonalidadPorPerfil(idPerfil: Int): Map<String, String>? {
        val db = dbHelper.readableDatabase
        var resultado: MutableMap<String, String>? = null

        try {
            val cursor = db.rawQuery(
                "SELECT comportamiento, entorno, interaccion_social FROM PERSONALIDADES WHERE id_perfil = ?",
                arrayOf(idPerfil.toString())
            )

            if (cursor.moveToFirst()) {
                resultado = mutableMapOf(
                    "comportamiento" to cursor.getString(0),
                    "entorno" to cursor.getString(1),
                    "interaccion_social" to cursor.getString(2)
                )
            }

            cursor.close()

        } catch (e: Exception) {
            Log.e("PersonalidadDAO", "❌ Error al obtener personalidad", e)
        } finally {
            db.close()
        }

        return resultado
    }

    /**
     * Actualiza una personalidad existente (por si el usuario edita su perfil).
     */
    fun actualizarPersonalidad(
        comportamiento: String,
        entorno: String,
        interaccionSocial: String
    ): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        val idPerfil = session.obtenerIdPerfil()

        if (idPerfil == -1) {
            Log.e("PersonalidadDAO", "❌ No se encontró ID de perfil en sesión.")
            return false
        }

        try {
            val valores = ContentValues().apply {
                put("comportamiento", comportamiento)
                put("entorno", entorno)
                put("interaccion_social", interaccionSocial)
            }

            val filas = db.update(
                "PERSONALIDADES",
                valores,
                "id_perfil = ?",
                arrayOf(idPerfil.toString())
            )

            if (filas > 0) {
                Log.d("PersonalidadDAO", "♻️ Personalidad actualizada correctamente.")
                exito = true
            }

        } catch (e: SQLException) {
            Log.e("PersonalidadDAO", "❌ Error SQL al actualizar personalidad", e)
        } finally {
            db.close()
        }

        return exito
    }

    /**
     * Elimina la personalidad de un perfil (si se reinicia el flujo).
     */
    fun eliminarPersonalidadPorPerfil(idPerfil: Int): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        try {
            val filas = db.delete("PERSONALIDADES", "id_perfil = ?", arrayOf(idPerfil.toString()))
            if (filas > 0) {
                Log.d("PersonalidadDAO", "🧹 Personalidad eliminada para perfil ID=$idPerfil")
                exito = true
            }
        } catch (e: Exception) {
            Log.e("PersonalidadDAO", "❌ Error al eliminar personalidad", e)
        } finally {
            db.close()
        }
        return exito
    }
}
