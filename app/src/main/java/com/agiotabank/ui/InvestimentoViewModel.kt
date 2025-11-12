package com.agiotabank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.Investimento
import com.agiotabank.data.InvestimentoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// 1. Classe de Estado dedicada, como você sugeriu
data class InvestmentState(
    val totalInvestido: Double = 0.0,
    val investimentos: List<Investimento> = emptyList(),
    val isLoading: Boolean = false
)

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class InvestimentoViewModel @Inject constructor(
    private val repository: InvestimentoRepository,
    contaRepository: ContaRepository
) : ViewModel() {

    private val _state = MutableStateFlow(InvestmentState())
    val state: StateFlow<InvestmentState> = _state.asStateFlow()

    private val contaIdFlow = contaRepository.contaIdFlow

    init {
        // 2. Carregar o estado inicial
        loadInvestments()
    }

    private fun loadInvestments() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            // 3. Usamos flatMapLatest para reagir a mudanças de login
            contaIdFlow.flatMapLatest { contaId ->
                if (contaId != null) {
                    repository.getInvestimentos(contaId)
                } else {
                    flowOf(emptyList()) // Retorna vazio se deslogado
                }
            }.collect { investimentos ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        investimentos = investimentos,
                        totalInvestido = investimentos.sumOf { inv -> inv.valorCompra }
                    )
                }
            }
        }
    }

    // 4. Função para a tela de detalhes
    suspend fun getInvestimentoById(id: Long): Investimento? {
        // Simples, mas para um app real, o ideal seria buscar no repository
        return _state.value.investimentos.find { it.id == id }
    }

    // 5. Ação de comprar (simplificada)
    fun comprarInvestimento(
        nomeAtivo: String,
        tipoAtivo: String,
        valorCompra: Double,
        quantidade: Double
    ) {
        viewModelScope.launch {
            val contaId = contaIdFlow.first() ?: return@launch // Pega o ID atual
            val novoInvestimento = Investimento(
                contaId = contaId,
                nomeAtivo = nomeAtivo,
                tipoAtivo = tipoAtivo,
                valorCompra = valorCompra,
                quantidade = quantidade
            )
            repository.comprarInvestimento(novoInvestimento)
            // O flow 'collect' no init() atualizará a UI automaticamente
        }
    }
}