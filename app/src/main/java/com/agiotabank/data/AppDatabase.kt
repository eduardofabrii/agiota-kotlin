package com.agiotabank.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Conta::class, Transacao::class, Card::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contaDao(): ContaDao
    abstract fun transacaoDao(): TransacaoDao
    abstract fun cardDao(): CardDao
}
