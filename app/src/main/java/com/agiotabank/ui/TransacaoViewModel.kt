package com.agiotabank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.ContaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TransacaoViewModel @Inject constructor(
    private val repo: ContaRepository
) : ViewModel() {

    fun transferir(deContaId: Long, paraContaId: Long, valor: Double, onResult: (Throwable?) -> Unit) {
        viewModelScope.launch {
            try {
                repo.transferir(deContaId, paraContaId, valor)
                onResult(null)
            } catch (t: Throwable) {
                onResult(t)
            }
        }
    }
}
