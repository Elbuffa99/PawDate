package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "PERFILES")
data class Perfil(
    @PrimaryKey(autoGenerate = true)
    val id_perfil: Int = 0,
    val email: String,
    val telefono: String,
    val nombre_perro: String,
    val fecha_nacimiento: Date,
    val genero: String,   // Macho / Hembra
    val busca: String,     // Qué busca el perro
    val relaciones: String,
    val avatar: String?    // Imagen en Base64
)
