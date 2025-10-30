package org.cibertec.edu.interfacesproyecto.model.dao

import androidx.room.*
import org.cibertec.edu.interfacesproyecto.model.entidades.Match

@Dao
interface MatchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(match: Match)

    @Update
    suspend fun actualizar(match: Match)

    @Delete
    suspend fun eliminar(match: Match)

    @Query("SELECT * FROM MATCHES")
    suspend fun listar(): List<Match>

    @Query("SELECT * FROM MATCHES WHERE id_match = :id")
    suspend fun obtenerPorId(id: Int): Match?

    @Query("SELECT * FROM MATCHES WHERE id_perro1 = :idPerro OR id_perro2 = :idPerro")
    suspend fun listarPorPerro(idPerro: Int): List<Match>
}
