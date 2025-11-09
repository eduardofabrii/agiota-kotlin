package com.agiotabank.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
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
data class Card(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val holderName: String,
    val number: String,
    val expiryDate: String,
    val cvv: String,
    val contaId: Long
)
