package com.agiotabank.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.ContaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class TransacaoViewModel @Inject constructor(

) : ViewModel() {
}
