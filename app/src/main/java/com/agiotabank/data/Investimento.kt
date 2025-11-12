package com.agiotabank.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "investimentos",
    foreignKeys = [
        ForeignKey(
            entity = Conta::class,
            parentColumns = ["id"],
            childColumns = ["contaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["contaId"])]
)
data class Investimento(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val contaId: Long,
    val nomeAtivo: String, // ex: "Ação BR"
    val tipoAtivo: String, // ex: "Ação"
    val valorCompra: Double,
    val quantidade: Double,
    val timestamp: Long = System.currentTimeMillis()
)