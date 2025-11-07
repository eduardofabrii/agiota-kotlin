package com.agiotabank.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton


class ContaRepository(
    private val contaDao: ContaDao
) {
    suspend fun updateSaldo(conta: Conta, valor: Double): Int = withContext(Dispatchers.IO) {
        contaDao.updateSaldo(conta.id, valor)
    }

    suspend fun criarConta(
        nome: String,
        email: String,
        senha: String,
        saldoInicial: Double,
        tipo: TipoConta
    ): Conta = withContext(Dispatchers.IO) {
        val temp = Conta(
            nome = nome,
            email = email,
            senha = senha,
            saldo = saldoInicial,
            tipo = tipo
        )
        val id = contaDao.insert(temp)
        temp.copy(id = id)
    }

    suspend fun login(email: String, senha: String): Conta? = withContext(Dispatchers.IO) {
        contaDao.login(email, senha)
    }

    suspend fun remover(conta: Conta) = withContext(Dispatchers.IO) {
        contaDao.remover(conta)
    }

}
