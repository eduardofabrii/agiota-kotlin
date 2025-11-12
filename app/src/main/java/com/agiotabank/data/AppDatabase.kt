package com.agiotabank.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Conta::class, Transacao::class, Card::class, PixKey::class, Emprestimo::class, Investimento::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contaDao(): ContaDao
    abstract fun transacaoDao(): TransacaoDao
    abstract fun cardDao(): CardDao
    abstract fun pixKeyDao(): PixKeyDao
    abstract fun emprestimoDao(): EmprestimoDao
    abstract fun investimentoDao(): InvestimentoDao
}
