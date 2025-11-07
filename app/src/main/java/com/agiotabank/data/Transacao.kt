package com.agiotabank.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transacoes")
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val deContaId: Long,
    val paraContaId: Long,
    val valor: Double,
    val timestamp: Long = System.currentTimeMillis()
)
