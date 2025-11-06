package com.agiotabank.data

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.random.Random

@Entity(
    tableName = "contas",
    indices = [Index(value = ["numero"], unique = true)]
)
data class Conta(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nome: String,
    val idade: Int,
    val email: String,
    val senha: String,
    var saldo: Double = 0.0,
    val tipo: TipoConta,
    val agencia: String = "0001",
    val numero: String = gerarNumero()
)

enum class TipoConta {
    CORRENTE, POUPANCA
}

private fun gerarNumero(): String = String.format("%011d", Random.nextLong(0L, 100_000_000_000L))