package com.agiotabank.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.Emprestimo
import com.agiotabank.data.EmprestimoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmprestimoViewModel @Inject constructor(
    private val emprestimoRepository: EmprestimoRepository,
    private val contaRepository: ContaRepository
) : ViewModel() {

    val emprestimos: StateFlow<List<Emprestimo>> = contaRepository.contaIdFlow.flatMapLatest { contaId ->
        contaId?.let { emprestimoRepository.getEmprestimos(it) } ?: flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun solicitarEmprestimo(valor: Double, parcelas: Int) {
        viewModelScope.launch {
            val contaId = contaRepository.contaIdFlow.first() ?: return@launch
            Log.d("EmprestimoViewModel", "Solicitar emprestimo para $contaId")
            // Simplificação da taxa de juros para o exemplo
            val taxaJuros = 0.05 // 5%
            emprestimoRepository.solicitarEmprestimo(contaId, valor, parcelas, taxaJuros)
        }
    }
}
