// app/src/main/java/com/agiotabank/data/ContaRepository.kt
package com.agiotabank.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContaRepository(
    private val contaDao: ContaDao,
    private val transacaoDao: TransacaoDao
) {
    suspend fun criarConta(nome: String, idade: Int, email: String, senha: String, saldoInicial: Double, tipo: TipoConta): Conta = withContext(Dispatchers.IO) {
        val temp = Conta(
            nome = nome,
            idade = idade,
            email = email,
            senha = senha,
            saldo = saldoInicial,
            tipo = tipo
        )
        val id = contaDao.insert(temp)
        temp.copy(id = id)
    }
}
