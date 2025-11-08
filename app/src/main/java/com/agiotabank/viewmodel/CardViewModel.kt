package com.agiotabank.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.agiotabank.data.Card
import com.agiotabank.data.CardDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(private val cardDao: CardDao) : ViewModel() {

    val cards = cardDao.getAllCards()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun addCard(card: Card) {
        viewModelScope.launch {
            cardDao.insertCard(card)
        }
    }

    fun deleteCard(card: Card) {
        viewModelScope.launch {
            cardDao.deleteCard(card)
        }
    }
}
