package com.agiotabank.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity
data class Card(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val holderName: String,
    val number: String,
    val expiryDate: String,
    val cvv: String
)
