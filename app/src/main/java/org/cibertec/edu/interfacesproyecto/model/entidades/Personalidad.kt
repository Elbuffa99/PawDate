package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PERSONALIDAD",
    foreignKeys = [
        ForeignKey(
            entity = Perro::class,
            parentColumns = ["id_perro"],
            childColumns = ["id_perro"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Personalidad(
    @PrimaryKey(autoGenerate = true)
    val id_personalidad: Int = 0,
    val id_perro: Int,
    val comportamiento: String,
    val entorno: String,
    val interaccion_social: String
)
