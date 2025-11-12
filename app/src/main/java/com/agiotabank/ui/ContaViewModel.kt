package com.agiotabank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.Conta
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.TipoConta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContaViewModel @Inject constructor(
    private val repo: ContaRepository
) : ViewModel() {

    val contaId: StateFlow<Long?> = repo.contaIdFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    val contaLogada: StateFlow<Conta?> = repo.contaLogadaFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, null)
    fun criar(
        nome: String,
        email: String,
        senha: String,
        cpf: String,
        telefone: String,
        saldoInicial: Double = 5000.0,
        tipo: TipoConta = TipoConta.CORRENTE
    ) = viewModelScope.launch {
        repo.criarConta(
            nome, email, senha, cpf, telefone, saldoInicial, tipo
        )
    }

    fun login(email: String, senha: String, onLoginSuccess: () -> Unit) = viewModelScope.launch {
        repo.login(email, senha)
        onLoginSuccess()
    }

    suspend fun findContaByPixKey(chave: String): Conta? {
        return repo.findByPixKey(chave)
    }

    suspend fun findContaByAgenciaAndNumero(agencia: String, numero: String): Conta? {
        return repo.findByAgenciaAndNumero(agencia, numero)
    }

    suspend fun findContaById(id: Long): Conta? {
        return repo.getContaById(id).firstOrNull()
    }

    suspend fun logout() = repo.logout()

    fun updateNome(novoNome: String) = viewModelScope.launch {
        contaLogada.value?.let { contaAtual ->
            val contaAtualizada = contaAtual.copy(nome = novoNome)
            repo.updateConta(contaAtualizada)
        }
    }
    fun excluir() = viewModelScope.launch { contaLogada.value?.let { repo.remover(it) } }
}
