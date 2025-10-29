package org.cibertec.edu.interfacesproyecto.model.dao

import androidx.room.*
import org.cibertec.edu.interfacesproyecto.model.entidades.Perro

@Dao
interface PerroDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(perro: Perro)

    @Update
    suspend fun actualizar(perro: Perro)

    @Delete
    suspend fun eliminar(perro: Perro)

    @Query("SELECT * FROM PERROS")
    suspend fun listar(): List<Perro>

    @Query("SELECT * FROM PERROS WHERE id_perro = :id")
    suspend fun obtenerPorId(id: Int): Perro?
}
