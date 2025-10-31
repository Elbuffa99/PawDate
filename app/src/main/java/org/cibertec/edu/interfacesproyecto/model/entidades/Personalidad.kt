package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "PERSONALIDADES",
    foreignKeys = [
        ForeignKey(
            entity = Perfil::class,
            parentColumns = ["id_perfil"],
            childColumns = ["id_perfil"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Personalidad(
    @PrimaryKey(autoGenerate = true)
    val id_personalidad: Int = 0,
    val id_perfil: Int, // 🔗 Relación con Perfil
    val comportamiento: String,
    val entorno: String,
    val interaccion_social: String
)
