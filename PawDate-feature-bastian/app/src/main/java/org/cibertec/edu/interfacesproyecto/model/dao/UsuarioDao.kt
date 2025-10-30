package org.cibertec.edu.interfacesproyecto.model.dao

import androidx.room.*
import org.cibertec.edu.interfacesproyecto.model.entidades.Usuario

@Dao
interface UsuarioDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(usuario: Usuario)

    @Update
    suspend fun actualizar(usuario: Usuario)

    @Delete
    suspend fun eliminar(usuario: Usuario)

    @Query("SELECT * FROM USUARIOS")
    suspend fun listar(): List<Usuario>

    @Query("SELECT * FROM USUARIOS WHERE id_usuario = :id")
    suspend fun obtenerPorId(id: Int): Usuario?
}
