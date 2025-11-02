package org.cibertec.edu.interfacesproyecto.model.entidades

data class Match(
    val id_match: Int = 0,
    val id_perfil1: Int,
    val id_perfil2: Int,
    val estado: String,       // "aceptado" o "rechazado"
    val fecha_match: Long = System.currentTimeMillis()
)
