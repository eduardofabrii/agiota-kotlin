package com.agiotabank.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

enum class EmprestimoStatus {
    ATIVO,
    QUITADO
}

@Entity(
    tableName = "emprestimos",
    foreignKeys = [
        ForeignKey(
            entity = Conta::class,
            parentColumns = ["id"],
            childColumns = ["contaId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Emprestimo(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val contaId: Long,
    val valorSolicitado: Double,
    val valorTotal: Double,
    val juros: Double,
    val numeroParcelas: Int,
    val dataSolicitacao: Long,
    val status: EmprestimoStatus = EmprestimoStatus.ATIVO
)
