package org.cibertec.edu.interfacesproyecto.controller

import android.content.Context
import android.content.SharedPreferences

class RegistroTemporalManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("registro_temporal", Context.MODE_PRIVATE)

    fun guardarDato(clave: String, valor: String) {
        prefs.edit().putString(clave, valor).apply()
    }

    fun obtenerDato(clave: String): String? = prefs.getString(clave, null)

    fun limpiar() {
        prefs.edit().clear().apply()
    }
}
