package com.agiotabank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TransacaoDao {
    @Insert
    suspend fun insert(transacao: Transacao): Long

    @Query("SELECT * FROM transacoes WHERE deContaId = :contaId OR paraContaId = :contaId")
    fun getByContaId(contaId: Long): Flow<List<Transacao>>
}
