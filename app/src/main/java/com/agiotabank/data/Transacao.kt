package com.agiotabank.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "transacoes",
    foreignKeys = [
        ForeignKey(
            entity = Conta::class,
            parentColumns = ["id"],
            childColumns = ["deContaId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Conta::class,
            parentColumns = ["id"],
            childColumns = ["paraContaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["deContaId"]),
        Index(value = ["paraContaId"])
    ]
)
data class Transacao(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val deContaId: Long,
    val paraContaId: Long,
    val valor: Double,
    val timestamp: Long = System.currentTimeMillis()
)
