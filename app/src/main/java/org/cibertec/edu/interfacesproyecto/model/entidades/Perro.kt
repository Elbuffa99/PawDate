package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PERROS",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id_usuario"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Perro(
    @PrimaryKey(autoGenerate = true)
    val id_perro: Int = 0,
    val id_usuario: Int,
    val nombre_perro: String,
    val fecha_nacimiento: String,
    val genero: String,
    val busca: String
)
