package com.agiotabank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContaDao {
    @Query("SELECT * FROM contas WHERE id = :id")
    suspend fun getById(id: Long): Conta?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(conta: Conta): Long

    @Query("UPDATE contas SET saldo = saldo + :valor WHERE id = :id")
    suspend fun updateSaldo(id: Long, valor: Double): Int
}
