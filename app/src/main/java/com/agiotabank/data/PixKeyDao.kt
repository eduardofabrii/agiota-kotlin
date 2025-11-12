package com.agiotabank.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PixKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pixKey: PixKey)

    @Query("SELECT * FROM pix_keys WHERE contaId = :contaId")
    fun getKeysByContaId(contaId: Long): Flow<List<PixKey>>

    @Query("SELECT * FROM pix_keys WHERE `key` = :chave")
    fun getPixKeyByChave(chave: String): Flow<PixKey?>
}
