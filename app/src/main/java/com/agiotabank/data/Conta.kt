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
    val email: String,
    val senha: String,
    val cpf: String,
    val telefone: String,
    val saldo: Double = 5000.0,
    val tipo: TipoConta = TipoConta.CORRENTE,
    val agencia: String = "0001",
    val numero: String = gerarNumero()
)

enum class TipoConta {
    CORRENTE, POUPANCA
}

private fun gerarNumero(): String = String.format("%011d", Random.nextLong(0L, 100_000_000_000L))