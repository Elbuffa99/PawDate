package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "HABITOS",
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
data class Habito(
    @PrimaryKey(autoGenerate = true)
    val id_habito: Int = 0,
    val id_perfil: Int, // 🔗 Relación con Perfil
    val nivel_energia: String,
    val frecuencia_paseos: String,
    val sociabilidad: String,
    val alimentacion: String,
    val horarios_descanso: String
)
