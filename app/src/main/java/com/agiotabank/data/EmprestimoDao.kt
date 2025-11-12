package com.agiotabank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface EmprestimoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(emprestimo: Emprestimo)

    @Query("SELECT * FROM emprestimos WHERE contaId = :contaId ORDER BY dataSolicitacao DESC")
    fun getEmprestimosByContaId(contaId: Long): Flow<List<Emprestimo>>
}
