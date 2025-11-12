package com.agiotabank.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.nio.file.Files.delete

@Dao
interface ContaDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(conta: Conta): Long

    @Query("UPDATE contas SET saldo = saldo + :valor WHERE id = :id")
    suspend fun updateSaldo(id: Long, valor: Double): Int

    @Query("SELECT * FROM contas WHERE id = :id")
    fun getById(id: Long): Flow<Conta?>

    @Query("SELECT * FROM contas WHERE email = :email AND senha = :senha")
    suspend fun login(email: String, senha: String): Conta?

    @Update
    suspend fun update(conta: Conta)

    @Query("SELECT * FROM contas WHERE email = :chave OR cpf = :chave OR telefone = :chave")
    suspend fun findByPixKey(chave: String): Conta?

    @Query("SELECT * FROM contas WHERE agencia = :agencia and numero = :numero")
    suspend fun findByAgenciaAndNumero(agencia: String ,numero: String): Conta?

    @Delete
    suspend fun remover(conta: Conta)
}
