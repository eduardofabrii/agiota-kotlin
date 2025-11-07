package com.agiotabank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.Conta
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.TipoConta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContaViewModel @Inject constructor(
    private val repo: ContaRepository
) : ViewModel() {
    private val _contaLogada = MutableStateFlow<Conta?>(null)
    val contaLogada: StateFlow<Conta?> = _contaLogada.asStateFlow()

    fun criar(
        nome: String,
        email: String,
        senha: String,
        saldoInicial: Double = 5000.0,
        tipo: TipoConta = TipoConta.CORRENTE
    ) = viewModelScope.launch {
        repo.criarConta(
            nome, email, senha, saldoInicial, tipo
        )
    }

    fun login(email: String, senha: String, onLoginSuccess: () -> Unit) = viewModelScope.launch {
        val conta = repo.login(email, senha)
        conta?.let {
            _contaLogada.value = it
            onLoginSuccess()
        }

    }

    fun atualizarSaldo(valor: Double) = viewModelScope.launch {
        contaLogada.value?.let { conta ->
            repo.updateSaldo(conta = conta, valor)
        }
    }

    fun excluir() = viewModelScope.launch { contaLogada.value?.let { repo.remover(it) } }
}