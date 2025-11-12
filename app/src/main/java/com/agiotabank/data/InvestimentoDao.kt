package com.agiotabank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface InvestimentoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(investimento: Investimento)

    @Query("SELECT * FROM investimentos WHERE contaId = :contaId ORDER BY timestamp DESC")
    fun getInvestimentosPorContaId(contaId: Long): Flow<List<Investimento>>
}