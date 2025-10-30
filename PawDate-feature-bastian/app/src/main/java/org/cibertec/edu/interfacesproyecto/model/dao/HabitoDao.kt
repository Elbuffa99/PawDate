package org.cibertec.edu.interfacesproyecto.model.dao

import androidx.room.*
import org.cibertec.edu.interfacesproyecto.model.entidades.Habito

@Dao
interface HabitoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(habito: Habito)

    @Update
    suspend fun actualizar(habito: Habito)

    @Delete
    suspend fun eliminar(habito: Habito)

    @Query("SELECT * FROM HABITOS")
    suspend fun listar(): List<Habito>

    @Query("SELECT * FROM HABITOS WHERE id_habito = :id")
    suspend fun obtenerPorId(id: Int): Habito?

    @Query("SELECT * FROM HABITOS WHERE id_perro = :idPerro")
    suspend fun obtenerPorPerro(idPerro: Int): Habito?
}
