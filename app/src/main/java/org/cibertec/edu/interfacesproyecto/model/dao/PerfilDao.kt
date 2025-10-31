package org.cibertec.edu.interfacesproyecto.model.dao

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.util.Log
import org.cibertec.edu.interfacesproyecto.controller.SessionManager
import org.cibertec.edu.interfacesproyecto.model.db.PawDateDBHelper

class PerfilDAO(private val context: Context) {

    private val dbHelper = PawDateDBHelper(context)
    private val session = SessionManager(context)

    // ✅ 1️⃣ Inserta un nuevo perfil
    fun insertarPerfil(
        email: String,
        telefono: String,
        nombrePerro: String,
        fechaNacimiento: Long,
        genero: String,
        busca: String   // ← ahora este valor viene directamente desde la interfaz
    ): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase

        try {
            val valores = ContentValues().apply {
                put("email", email)
                put("telefono", telefono)
                put("nombre_perro", nombrePerro)
                put("fecha_nacimiento", fechaNacimiento)
                put("genero", genero)
                put("busca", busca)       // ← valor dinámico según lo que seleccione el usuario
                put("relaciones", "")     // opcional: se inicializa vacío
                put("avatar", "")          // opcional: se inicializa vacío
            }

            val id = db.insertOrThrow("PERFILES", null, valores)
            if (id > 0) {
                session.guardarIdPerfil(id.toInt())
                Log.d("PerfilDAO", "✅ Perfil insertado con ID: $id (busca='$busca')")
                Log.d("PerfilDAO", "🔐 ID guardado en SessionManager: ${session.obtenerIdPerfil()}")
                exito = true
            }else {
                Log.e("PerfilDAO", "❌ Error: la inserción devolvió ID=$id")
            }

        } catch (e: SQLException) {
            Log.e("PerfilDAO", "❌ Error SQL al insertar perfil", e)
        } catch (e: Exception) {
            Log.e("PerfilDAO", "❌ Error inesperado al insertar perfil", e)
        } finally {
            db.close()
        }

        return exito
    }


    // ✅ 2️⃣ Actualiza el avatar (imagen en base64)
    fun actualizarAvatar(avatarBase64: String): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        val idPerfil = session.obtenerIdPerfil()

        if (idPerfil == -1) {
            Log.e("PerfilDAO", "❌ No hay perfil en sesión.")
            return false
        }

        try {
            val valores = ContentValues().apply {
                put("avatar", avatarBase64)
            }

            val filas = db.update(
                "PERFILES",
                valores,
                "id_perfil = ?",
                arrayOf(idPerfil.toString())
            )

            if (filas > 0) {
                Log.d("PerfilDAO", "🖼️ Avatar actualizado correctamente para ID=$idPerfil")
                exito = true
            }

        } catch (e: SQLException) {
            Log.e("PerfilDAO", "❌ Error SQL al actualizar avatar", e)
        } catch (e: Exception) {
            Log.e("PerfilDAO", "❌ Error inesperado al actualizar avatar", e)
        } finally {
            db.close()
        }

        return exito
    }

    // ✅ 2️⃣-bis: Actualiza la relación elegida (campo relaciones)
    fun actualizarRelaciones(relaciones: String): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        val idPerfil = session.obtenerIdPerfil()

        if (idPerfil == -1) {
            Log.e("PerfilDAO", "❌ No hay perfil activo en sesión para actualizar relaciones.")
            return false
        }

        try {
            val valores = ContentValues().apply {
                put("relaciones", relaciones)
            }

            val filas = db.update(
                "PERFILES",
                valores,
                "id_perfil = ?",
                arrayOf(idPerfil.toString())
            )

            if (filas > 0) {
                Log.d("PerfilDAO", "💛 Relaciones actualizadas correctamente para ID=$idPerfil → '$relaciones'")
                exito = true
            } else {
                Log.w("PerfilDAO", "⚠️ No se actualizó ninguna fila. ¿Existe el perfil ID=$idPerfil?")
            }

        } catch (e: SQLException) {
            Log.e("PerfilDAO", "❌ Error SQL al actualizar relaciones", e)
        } catch (e: Exception) {
            Log.e("PerfilDAO", "❌ Error inesperado al actualizar relaciones", e)
        } finally {
            db.close()
        }

        return exito
    }



    // ✅ 3️⃣ Recupera los datos del perfil según su ID
    fun obtenerPerfilPorId(idPerfil: Int): Map<String, String>? {
        val db = dbHelper.readableDatabase
        var resultado: MutableMap<String, String>? = null

        try {
            val cursor = db.rawQuery(
                """
                SELECT email, telefono, nombre_perro, fecha_nacimiento, genero, busca, avatar
                FROM PERFILES
                WHERE id_perfil = ?
                """.trimIndent(),
                arrayOf(idPerfil.toString())
            )

            if (cursor.moveToFirst()) {
                resultado = mutableMapOf(
                    "email" to cursor.getString(0),
                    "telefono" to cursor.getString(1),
                    "nombre_perro" to cursor.getString(2),
                    "fecha_nacimiento" to cursor.getString(3),
                    "genero" to cursor.getString(4),
                    "busca" to cursor.getString(5),
                    "avatar" to cursor.getString(6)
                )
                Log.d("PerfilDAO", "📄 Perfil obtenido: $resultado")
            }

            cursor.close()

        } catch (e: Exception) {
            Log.e("PerfilDAO", "❌ Error al obtener perfil por ID", e)
        } finally {
            db.close()
        }

        return resultado
    }

    // ✅ 4️⃣ Elimina un perfil y limpia la sesión
    fun eliminarPerfil(idPerfil: Int): Boolean {
        var exito = false
        val db = dbHelper.writableDatabase
        try {
            val filas = db.delete("PERFILES", "id_perfil = ?", arrayOf(idPerfil.toString()))
            if (filas > 0) {
                session.limpiarSesion()
                Log.d("PerfilDAO", "🧹 Perfil eliminado ID=$idPerfil")
                exito = true
            }
        } catch (e: Exception) {
            Log.e("PerfilDAO", "❌ Error al eliminar perfil", e)
        } finally {
            db.close()
        }
        return exito
    }
}
