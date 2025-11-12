package com.agiotabank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.ContaRepository
import com.agiotabank.data.PixKey
import com.agiotabank.data.PixKeyRepository
import com.agiotabank.data.PixKeyType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PixKeyViewModel @Inject constructor(
    private val repository: PixKeyRepository,
    private val contaRepository: ContaRepository
) : ViewModel() {

    @OptIn(ExperimentalCoroutinesApi::class)
    val pixKeys: StateFlow<List<PixKey>> = contaRepository.contaIdFlow.flatMapLatest { contaId ->
        contaId?.let { repository.getPixKeys(it) } ?: flowOf(emptyList())
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    
    suspend fun pixKeyByChave(chave: String): PixKey? {
        return repository.getPixKeyByChave(chave).firstOrNull()
    }

    fun createPixKey(type: PixKeyType, key: String) {
        viewModelScope.launch {
            val contaId = contaRepository.contaIdFlow.first() ?: return@launch
            val pixKey = PixKey(type = type, key = key, contaId = contaId)
            repository.createPixKey(pixKey)
        }
    }
}
