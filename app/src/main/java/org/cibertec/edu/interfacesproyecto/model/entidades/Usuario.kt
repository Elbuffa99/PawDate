package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "USUARIOS")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id_usuario: Int = 0,
    val nombre: String,
    val email: String,
    val telefono: String
)
