package com.agiotabank.data

import androidx.room.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InvestimentoRepository @Inject constructor(
    private val investimentoDao: InvestimentoDao,
    private val contaDao: ContaDao
) {
    fun getInvestimentos(contaId: Long): Flow<List<Investimento>> {
        return investimentoDao.getInvestimentosPorContaId(contaId)
    }

    @Transaction
    suspend fun comprarInvestimento(investimento: Investimento) = withContext(Dispatchers.IO) {
        // 1. Salva o registro do investimento
        investimentoDao.insert(investimento)

        // 2. Deduz o valor do saldo da conta
        contaDao.updateSaldo(investimento.contaId, -investimento.valorCompra)
    }
}