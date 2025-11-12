package com.agiotabank.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class PixKeyType {
    CPF,
    EMAIL,
    PHONE,
    RANDOM
}

@Entity(
    tableName = "pix_keys",
    foreignKeys = [
        ForeignKey(
            entity = Conta::class,
            parentColumns = ["id"],
            childColumns = ["contaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PixKey(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: PixKeyType,
    val key: String,
    val contaId: Long
)
