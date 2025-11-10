package com.agiotabank.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TransacaoRepository(
    private val transacaoDao: TransacaoDao,
    private val contaDao: ContaDao
) {
    suspend fun realizarTransacao(deConta: Conta, paraConta: Conta, valor: Double) = withContext(Dispatchers.IO) {
        val transacao = Transacao(
            deContaId = deConta.id,
            paraContaId = paraConta.id,
            valor = valor
        )
        Log.d("TransacaoRepository", "Inserindo transacao: $transacao")
        transacaoDao.insert(transacao)
        contaDao.updateSaldo(deConta.id, -valor)
        contaDao.updateSaldo(paraConta.id, valor)
    }

    fun getTransacoesByContaId(contaId: Long): Flow<List<Transacao>> {
        return transacaoDao.getByContaId(contaId)
    }
}
