package com.agiotabank.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Conta::class, Transacao::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun contaDao(): ContaDao
    abstract fun transacaoDao(): TransacaoDao
}
