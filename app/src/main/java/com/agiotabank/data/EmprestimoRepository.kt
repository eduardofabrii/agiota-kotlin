package com.agiotabank.data

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmprestimoRepository @Inject constructor(
    private val emprestimoDao: EmprestimoDao,
    private val contaDao: ContaDao
) {

    fun getEmprestimos(contaId: Long): Flow<List<Emprestimo>> {
        return emprestimoDao.getEmprestimosByContaId(contaId)
    }

    suspend fun solicitarEmprestimo(
        contaId: Long,
        valor: Double,
        parcelas: Int,
        taxaJuros: Double
    ) {
        val valorTotal = valor * (1 + taxaJuros)
        val emprestimo = Emprestimo(
            contaId = contaId,
            valorSolicitado = valor,
            valorTotal = valorTotal,
            juros = taxaJuros,
            numeroParcelas = parcelas,
            dataSolicitacao = System.currentTimeMillis()
        )
        emprestimoDao.insert(emprestimo)
        // Adiciona o valor do empréstimo ao saldo da conta
        contaDao.updateSaldo(contaId, valor)
    }
}
