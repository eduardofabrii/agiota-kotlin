package com.agiotabank.ui

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.Conta
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.Transacao
import com.agiotabank.data.TransacaoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransacaoViewModel @Inject constructor(
    private val repository: TransacaoRepository,
    private val contaRepository: ContaRepository
) : ViewModel() {
    @OptIn(ExperimentalCoroutinesApi::class)
    val transacoes: StateFlow<List<Transacao>> = contaRepository.contaLogadaFlow
        .flatMapLatest { conta ->
            if (conta != null) {
                repository.getTransacoesByContaId(conta.id)
            } else {
                flowOf(emptyList())
            }
        }
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun realizarTransacao(deConta: Conta, paraConta: Conta, valor: Double) {
        viewModelScope.launch {
            Log.d("TransacaoViewModel", "Realizar transacao para $paraConta.id")
            repository.realizarTransacao(deConta, paraConta, valor)
        }
    }
}
