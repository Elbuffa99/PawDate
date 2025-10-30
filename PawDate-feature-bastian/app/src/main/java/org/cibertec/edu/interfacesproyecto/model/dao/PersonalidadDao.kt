package org.cibertec.edu.interfacesproyecto.model.dao

import androidx.room.*
import org.cibertec.edu.interfacesproyecto.model.entidades.Personalidad

@Dao
interface PersonalidadDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(personalidad: Personalidad)

    @Update
    suspend fun actualizar(personalidad: Personalidad)

    @Delete
    suspend fun eliminar(personalidad: Personalidad)

    @Query("SELECT * FROM PERSONALIDAD")
    suspend fun listar(): List<Personalidad>

    @Query("SELECT * FROM PERSONALIDAD WHERE id_personalidad = :id")
    suspend fun obtenerPorId(id: Int): Personalidad?

    @Query("SELECT * FROM PERSONALIDAD WHERE id_perro = :idPerro")
    suspend fun obtenerPorPerro(idPerro: Int): Personalidad?
}
