package org.cibertec.edu.interfacesproyecto.controller

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

/**
 * SessionManager maneja los datos temporales del registro de perfil
 * usando SharedPreferences de forma segura.
 *
 * Permite:
 *  - Guardar temporalmente los datos del registro (antes de insertarlos en SQLite)
 *  - Guardar el id del perfil creado
 *  - Recuperar o limpiar los datos al finalizar el flujo
 */
class SessionManager(context: Context) {

    companion object {
        private const val PREF_NAME = "pawdate_session"
        private const val KEY_ID_PERFIL = "id_perfil_actual"

        // Claves base del registro
        private const val KEY_EMAIL = "email"
        private const val KEY_TELEFONO = "telefono"
        private const val KEY_NOMBRE_PERRO = "nombre_perro"
        private const val KEY_FECHA_NACIMIENTO = "fecha_nacimiento"
        private const val KEY_GENERO = "genero"
        private const val KEY_BUSCA = "busca"
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = prefs.edit()

    // ======================================================
    // 🔹 SECCIÓN 1: ID PERFIL
    // ======================================================
    fun guardarIdPerfil(idPerfil: Int) {
        try {
            editor.putInt(KEY_ID_PERFIL, idPerfil).apply()
            Log.d("SessionManager", "✅ ID perfil guardado: $idPerfil")
        } catch (e: Exception) {
            Log.e("SessionManager", "❌ Error al guardar ID perfil", e)
        }
    }

    fun obtenerIdPerfil(): Int {
        return try {
            prefs.getInt(KEY_ID_PERFIL, -1)
        } catch (e: Exception) {
            Log.e("SessionManager", "❌ Error al obtener ID perfil", e)
            -1
        }
    }

    // ======================================================
    // 🔹 SECCIÓN 2: DATOS TEMPORALES DEL REGISTRO
    // ======================================================
    fun guardarDatoTemporal(clave: String, valor: String) {
        try {
            editor.putString(clave, valor).apply()
            Log.d("SessionManager", "💾 Dato temporal guardado: $clave = $valor")
        } catch (e: Exception) {
            Log.e("SessionManager", "❌ Error al guardar dato temporal ($clave)", e)
        }
    }

    fun obtenerDatoTemporal(clave: String): String? {
        return try {
            prefs.getString(clave, null)
        } catch (e: Exception) {
            Log.e("SessionManager", "❌ Error al obtener dato temporal ($clave)", e)
            null
        }
    }

    // ======================================================
    // 🔹 SECCIÓN 3: MÉTODOS ESPECÍFICOS DE ACCESO
    // ======================================================
    fun guardarDatosContacto(email: String, telefono: String) {
        guardarDatoTemporal(KEY_EMAIL, email)
        guardarDatoTemporal(KEY_TELEFONO, telefono)
    }

    fun obtenerEmail(): String? = obtenerDatoTemporal(KEY_EMAIL)
    fun obtenerTelefono(): String? = obtenerDatoTemporal(KEY_TELEFONO)
    fun obtenerNombrePerro(): String? = obtenerDatoTemporal(KEY_NOMBRE_PERRO)
    fun obtenerFechaNacimiento(): String? = obtenerDatoTemporal(KEY_FECHA_NACIMIENTO)
    fun obtenerGenero(): String? = obtenerDatoTemporal(KEY_GENERO)
    fun obtenerBusca(): String? = obtenerDatoTemporal(KEY_BUSCA)

    // ======================================================
    // 🔹 SECCIÓN 4: LIMPIAR SESIÓN COMPLETA
    // ======================================================
    fun limpiarSesion() {
        try {
            editor.clear().apply()
            Log.d("SessionManager", "🧹 Sesión limpiada correctamente.")
        } catch (e: Exception) {
            Log.e("SessionManager", "❌ Error al limpiar la sesión", e)
        }
    }
}
