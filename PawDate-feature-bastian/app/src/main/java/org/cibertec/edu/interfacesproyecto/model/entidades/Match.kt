package org.cibertec.edu.interfacesproyecto.model.entidades

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "MATCHES",
    foreignKeys = [
        ForeignKey(
            entity = Perro::class,
            parentColumns = ["id_perro"],
            childColumns = ["id_perro1"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Perro::class,
            parentColumns = ["id_perro"],
            childColumns = ["id_perro2"],
            onDelete = ForeignKey.NO_ACTION,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Match(
    @PrimaryKey(autoGenerate = true)
    val id_match: Int = 0,
    val id_perro1: Int,
    val id_perro2: Int,
    val fecha_match: String,
    val estado: String
)
